package com.smartcity.urbanincident.controller;

import com.smartcity.urbanincident.entity.Notification;
import com.smartcity.urbanincident.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<Notification>> getNotificationsByRecipient(@PathVariable Long recipientId) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/recipient/{recipientId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long recipientId) {
        // Implementation will be added
        return null;
    }
    
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
}
