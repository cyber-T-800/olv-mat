package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.upece.granko.olvmat.entity.EventEntity;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    @Modifying
    @Query("update EventEntity e set e.stav='UNSELECTED' where e.stav='SELECTED'")
    void unselectCurrentEvent();

    @Modifying
    @Query("update EventEntity e set e.stav='SELECTED' where e.id=:id")
    void selectEvent(UUID id);

    @Query("select e from EventEntity e where e.stav='SELECTED'")
    Optional<EventEntity> findSelected();

}
