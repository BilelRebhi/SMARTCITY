package com.campusverse.municipal.controller;

import com.campusverse.municipal.dto.*;
import com.campusverse.municipal.model.*;
import com.campusverse.municipal.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/municipal")
@CrossOrigin(origins = "*")
public class MunicipalController {

    private final MunicipalServiceRepository serviceRepository;
    private final ApplicationRequestRepository applicationRepository;
    private final SupportingDocumentRepository documentRepository;
    private final AppointmentRepository appointmentRepository;
    private final MunicipalNotificationRepository notificationRepository;

    public MunicipalController(MunicipalServiceRepository serviceRepository,
                               ApplicationRequestRepository applicationRepository,
                               SupportingDocumentRepository documentRepository,
                               AppointmentRepository appointmentRepository,
                               MunicipalNotificationRepository notificationRepository) {
        this.serviceRepository = serviceRepository;
        this.applicationRepository = applicationRepository;
        this.documentRepository = documentRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationRepository = notificationRepository;
    }

    // ---------------------- 1. Services Catalogue ----------------------

    @GetMapping("/services")
    public List<MunicipalService> getAllServices() {
        return serviceRepository.findAll();
    }

    @GetMapping("/services/{id}")
    public MunicipalService getServiceById(@PathVariable UUID id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipal service not found"));
    }

    @PostMapping("/services")
    public ResponseEntity<MunicipalService> createService(@RequestBody MunicipalService service) {
        MunicipalService created = serviceRepository.save(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------- 2 & 6 & 9. Applications & History ----------------------

    @GetMapping("/applications")
    public List<ApplicationRequest> getAllApplications() {
        return applicationRepository.findAllByOrderBySubmittedAtDesc();
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationRequest> submitApplication(@RequestBody CreateApplicationDto dto) {
        if (dto.getServiceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID is required");
        }

        MunicipalService service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipal service not found"));

        ApplicationRequest request = new ApplicationRequest();
        request.setCitizenId(dto.getCitizenId() != null ? dto.getCitizenId() : UUID.randomUUID());
        request.setCitizenName(dto.getCitizenName());
        request.setCitizenEmail(dto.getCitizenEmail());
        request.setNotes(dto.getNotes());
        request.setService(service);
        request.setStatus(RequestStatus.PENDING);
        request.setValidatedComplete(false);

        ApplicationRequest saved = applicationRepository.save(request);

        createNotification(
                saved.getCitizenId(),
                saved.getId(),
                "Application Submitted",
                "Your request for \"" + service.getName() + "\" has been submitted and is pending validation.",
                "INFO"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/applications/{id}")
    public ApplicationRequest getApplicationById(@PathVariable UUID id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));
    }

    @GetMapping("/applications/citizen/{citizenId}")
    public List<ApplicationRequest> getApplicationsByCitizen(@PathVariable UUID citizenId) {
        return applicationRepository.findByCitizenIdOrderBySubmittedAtDesc(citizenId);
    }

    // ---------------------- 3. Supporting Document Upload ----------------------

    @PostMapping(value = "/applications/{id}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportingDocument> uploadDocument(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {

        ApplicationRequest request = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file cannot be empty");
        }

        SupportingDocument doc = new SupportingDocument();
        doc.setApplication(request);
        doc.setFileName(file.getOriginalFilename());
        doc.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        doc.setFileSize(file.getSize());
        doc.setData(file.getBytes());

        SupportingDocument saved = documentRepository.save(doc);

        // If citizen re-uploads while status was MISSING_DOCUMENTS, move back to PENDING for re-check
        if (request.getStatus() == RequestStatus.MISSING_DOCUMENTS) {
            request.setStatus(RequestStatus.PENDING);
            applicationRepository.save(request);
            createNotification(
                    request.getCitizenId(),
                    request.getId(),
                    "Supporting Document Uploaded",
                    "New document \"" + doc.getFileName() + "\" uploaded. Request returned to pending validation.",
                    "INFO"
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/applications/{id}/documents")
    public List<SupportingDocument> getApplicationDocuments(@PathVariable UUID id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found");
        }
        return documentRepository.findByApplicationIdOrderByUploadedAtDesc(id);
    }

    @GetMapping("/documents/{docId}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID docId) {
        SupportingDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(doc.getData());
    }

    // ---------------------- 4 & 5. Validation & Processing ----------------------

    @PatchMapping("/applications/{id}/validate")
    public ApplicationRequest validateApplication(@PathVariable UUID id, @RequestBody ValidateApplicationDto dto) {
        ApplicationRequest request = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));

        request.setValidatedComplete(dto.isComplete());
        if (dto.getComment() != null && !dto.getComment().isBlank()) {
            request.setAdminComment(dto.getComment());
        }

        if (dto.isComplete()) {
            if (request.getStatus() == RequestStatus.PENDING || request.getStatus() == RequestStatus.MISSING_DOCUMENTS) {
                request.setStatus(RequestStatus.IN_REVIEW);
            }
            createNotification(
                    request.getCitizenId(),
                    request.getId(),
                    "Application Validated",
                    "Your application for \"" + request.getService().getName() + "\" is complete and now in review.",
                    "INFO"
            );
        } else {
            request.setStatus(RequestStatus.MISSING_DOCUMENTS);
            String reason = (dto.getComment() != null && !dto.getComment().isBlank())
                    ? dto.getComment()
                    : "Supporting documents are incomplete or missing.";
            createNotification(
                    request.getCitizenId(),
                    request.getId(),
                    "Missing Documents Requested",
                    "Action required for \"" + request.getService().getName() + "\": " + reason,
                    "WARNING"
            );
        }

        return applicationRepository.save(request);
    }

    @PatchMapping("/applications/{id}/status")
    public ApplicationRequest updateApplicationStatus(@PathVariable UUID id, @RequestBody UpdateStatusDto dto) {
        ApplicationRequest request = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));

        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
            if (dto.getStatus() == RequestStatus.APPROVED
                    || dto.getStatus() == RequestStatus.REJECTED
                    || dto.getStatus() == RequestStatus.COMPLETED) {
                request.setCompletedAt(LocalDateTime.now());
            }
            if (dto.getStatus() == RequestStatus.MISSING_DOCUMENTS) {
                request.setValidatedComplete(false);
            }
        }
        if (dto.getComment() != null && !dto.getComment().isBlank()) {
            request.setAdminComment(dto.getComment());
        }

        ApplicationRequest updated = applicationRepository.save(request);

        String type = switch (updated.getStatus()) {
            case APPROVED, COMPLETED -> "SUCCESS";
            case REJECTED -> "ERROR";
            case MISSING_DOCUMENTS -> "WARNING";
            default -> "INFO";
        };
        String msg = "Your application for \"" + updated.getService().getName() + "\" status changed to " + updated.getStatus() + ".";
        if (updated.getAdminComment() != null && !updated.getAdminComment().isBlank()) {
            msg += " Note: " + updated.getAdminComment();
        }
        createNotification(updated.getCitizenId(), updated.getId(), "Application Status Update", msg, type);

        return updated;
    }

    // ---------------------- 7. Appointment Scheduling ----------------------

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> scheduleAppointment(@RequestBody CreateAppointmentDto dto) {
        if (dto.getApplicationId() == null || dto.getAppointmentDate() == null || dto.getTimeSlot() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application ID, date, and time slot are required");
        }

        ApplicationRequest application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request not found"));

        Appointment appointment = new Appointment();
        appointment.setApplication(application);
        appointment.setCitizenId(dto.getCitizenId() != null ? dto.getCitizenId() : application.getCitizenId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setTimeSlot(dto.getTimeSlot());
        appointment.setLocation(dto.getLocation() != null && !dto.getLocation().isBlank()
                ? dto.getLocation()
                : "City Hall - Municipal Counter B");
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        createNotification(
                saved.getCitizenId(),
                application.getId(),
                "Appointment Scheduled",
                "In-person appointment booked for " + saved.getAppointmentDate() + " at " + saved.getTimeSlot()
                        + " (" + saved.getLocation() + ").",
                "SUCCESS"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateAsc();
    }

    @GetMapping("/appointments/citizen/{citizenId}")
    public List<Appointment> getAppointmentsByCitizen(@PathVariable UUID citizenId) {
        return appointmentRepository.findByCitizenIdOrderByAppointmentDateAsc(citizenId);
    }

    @GetMapping("/appointments/application/{applicationId}")
    public List<Appointment> getAppointmentsByApplication(@PathVariable UUID applicationId) {
        return appointmentRepository.findByApplicationIdOrderByAppointmentDateAsc(applicationId);
    }

    @PatchMapping("/appointments/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        createNotification(
                saved.getCitizenId(),
                saved.getApplication().getId(),
                "Appointment Cancelled",
                "Your appointment on " + saved.getAppointmentDate() + " (" + saved.getTimeSlot() + ") was cancelled.",
                "WARNING"
        );

        return saved;
    }

    // ---------------------- 8. Administrative Notifications ----------------------

    @GetMapping("/notifications/citizen/{citizenId}")
    public List<MunicipalNotification> getNotificationsByCitizen(@PathVariable UUID citizenId) {
        return notificationRepository.findByCitizenIdOrderByCreatedAtDesc(citizenId);
    }

    @PatchMapping("/notifications/{id}/read")
    public MunicipalNotification markNotificationAsRead(@PathVariable UUID id) {
        MunicipalNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    // ---------------------- 10. Administrative Statistics ----------------------

    @GetMapping("/statistics")
    public MunicipalStatisticsDto getStatistics() {
        List<ApplicationRequest> all = applicationRepository.findAll();
        MunicipalStatisticsDto stats = new MunicipalStatisticsDto();
        stats.setTotalRequests(all.size());

        long pending = 0, inReview = 0, missing = 0, approved = 0, rejected = 0, completed = 0;
        double totalHours = 0;
        long processedWithTime = 0;

        Map<String, Long> byService = new LinkedHashMap<>();
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (RequestStatus rs : RequestStatus.values()) {
            byStatus.put(rs.name(), 0L);
        }

        for (ApplicationRequest req : all) {
            RequestStatus st = req.getStatus() != null ? req.getStatus() : RequestStatus.PENDING;
            byStatus.put(st.name(), byStatus.getOrDefault(st.name(), 0L) + 1);

            switch (st) {
                case PENDING -> pending++;
                case IN_REVIEW -> inReview++;
                case MISSING_DOCUMENTS -> missing++;
                case APPROVED -> approved++;
                case REJECTED -> rejected++;
                case COMPLETED -> completed++;
            }

            if (req.getService() != null && req.getService().getName() != null) {
                String sName = req.getService().getName();
                byService.put(sName, byService.getOrDefault(sName, 0L) + 1);
            }

            if (req.getSubmittedAt() != null && req.getCompletedAt() != null) {
                long minutes = Duration.between(req.getSubmittedAt(), req.getCompletedAt()).toMinutes();
                totalHours += Math.max(minutes, 1) / 60.0;
                processedWithTime++;
            }
        }

        stats.setPendingCount(pending);
        stats.setInReviewCount(inReview);
        stats.setMissingDocumentsCount(missing);
        stats.setApprovedCount(approved);
        stats.setRejectedCount(rejected);
        stats.setCompletedCount(completed);

        long decided = approved + completed + rejected;
        double approvalRate = decided > 0 ? ((double) (approved + completed) / decided) * 100.0 : 0.0;
        stats.setApprovalRate(Math.round(approvalRate * 10.0) / 10.0);

        double avgHours = processedWithTime > 0 ? totalHours / processedWithTime : 0.0;
        stats.setAverageProcessingTimeHours(Math.round(avgHours * 10.0) / 10.0);

        stats.setRequestsByService(byService);
        stats.setRequestsByStatus(byStatus);
        return stats;
    }

    // ---------------------- Helpers ----------------------

    private void createNotification(UUID citizenId, UUID applicationId, String title, String message, String type) {
        if (citizenId == null) return;
        MunicipalNotification notification = new MunicipalNotification();
        notification.setCitizenId(citizenId);
        notification.setApplicationId(applicationId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
