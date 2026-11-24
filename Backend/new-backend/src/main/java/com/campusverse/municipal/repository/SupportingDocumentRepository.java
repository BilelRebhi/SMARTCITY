package com.campusverse.municipal.repository;

import com.campusverse.municipal.model.SupportingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupportingDocumentRepository extends JpaRepository<SupportingDocument, UUID> {
    List<SupportingDocument> findByApplicationIdOrderByUploadedAtDesc(UUID applicationId);
}
