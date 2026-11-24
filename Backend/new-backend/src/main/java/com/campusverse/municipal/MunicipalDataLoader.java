package com.campusverse.municipal;

import com.campusverse.municipal.model.MunicipalService;
import com.campusverse.municipal.repository.MunicipalServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MunicipalDataLoader implements CommandLineRunner {

    private final MunicipalServiceRepository repository;

    public MunicipalDataLoader(MunicipalServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            createService(
                    "Campus Residence Certificate",
                    "Civil Documents",
                    "Official proof of campus residence for administrative and consular procedures.",
                    "National ID or Passport copy; Signed housing lease or dormitory attestation; Utility bill (< 3 months)",
                    2,
                    false
            );
            createService(
                    "Student Transport & Parking Card",
                    "Mobility & Permits",
                    "Subsidized transport pass and dedicated university parking authorization.",
                    "Student Enrollment Certificate; Vehicle registration (for parking); Passport-size photo",
                    3,
                    false
            );
            createService(
                    "Municipal Housing Aid & Grants",
                    "Financial Aid",
                    "Financial assistance and rent reduction certificates for enrolled students.",
                    "Tax return or income statement; Rental contract; Bank account IBAN; University enrollment proof",
                    5,
                    true
            );
            createService(
                    "Campus Civic Engagement & Volunteer Permit",
                    "Permits & Authorizations",
                    "Validation of student civic contribution and community service hours.",
                    "Host organization letter; Activity logbook; National ID copy",
                    4,
                    false
            );
            createService(
                    "Municipal Sports & Cultural Facility Pass",
                    "Sports & Culture",
                    "Access pass to town hall sports centers, pools, and public libraries.",
                    "Medical fitness certificate (for sports); Student ID card; Proof of residence",
                    1,
                    true
            );
        }
    }

    private void createService(String name, String category, String description,
                               String requirements, int delayDays, boolean requiresAppointment) {
        MunicipalService service = new MunicipalService();
        service.setName(name);
        service.setCategory(category);
        service.setDescription(description);
        service.setRequirements(requirements);
        service.setProcessingDelayDays(delayDays);
        service.setRequiresAppointment(requiresAppointment);
        repository.save(service);
    }
}
