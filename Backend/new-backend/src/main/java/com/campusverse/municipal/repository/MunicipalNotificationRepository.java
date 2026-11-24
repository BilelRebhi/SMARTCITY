package com.campusverse.municipal.repository;

import com.campusverse.municipal.model.MunicipalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MunicipalNotificationRepository extends JpaRepository<MunicipalNotification, UUID> {
    List<MunicipalNotification> findByCitizenIdOrderByCreatedAtDesc(UUID citizenId);
}
