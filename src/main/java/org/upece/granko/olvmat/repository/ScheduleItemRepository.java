package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.ScheduleItemEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItemEntity, UUID> {

    List<ScheduleItemEntity> findAllByEventIdOrderByOrder(UUID eventId);
}
