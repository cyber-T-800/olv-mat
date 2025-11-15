package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.AdminRegistraciaZiadostEntity;

import java.util.UUID;

@Repository
public interface AdminRegistraciaZiadostRepository extends JpaRepository<AdminRegistraciaZiadostEntity, UUID> {
}
