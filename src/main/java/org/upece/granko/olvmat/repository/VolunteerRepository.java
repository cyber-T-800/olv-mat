package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.upece.granko.olvmat.entity.VolunteerEntity;

import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<VolunteerEntity, UUID> {
}
