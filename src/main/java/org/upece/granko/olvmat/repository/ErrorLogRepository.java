package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.upece.granko.olvmat.entity.ErrorLogEntity;

import java.util.List;
import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, UUID> {
    List<ErrorLogEntity> findAllByOrderByCreatedAtDesc();
}
