package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.SellEventEntity;

import java.util.UUID;

@Repository
public interface SellEventRepository extends JpaRepository<SellEventEntity, UUID> {
}
