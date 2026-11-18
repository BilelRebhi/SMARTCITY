package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    
    Notification createNotification(Notification notification);
    
    Optional<Notification> getNotificationById(Long id);
    
    List<Notification> getNotificationsByRecipient(Long recipientId);
    
    List<Notification> getUnreadNotifications(Long recipientId);
    
    Notification markAsRead(Long id);
    
    List<Notification> getAllNotifications();
    
    void deleteNotification(Long id);
}
