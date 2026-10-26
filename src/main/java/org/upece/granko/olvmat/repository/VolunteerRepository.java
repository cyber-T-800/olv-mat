package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.VolunteerEntity;
import org.upece.granko.olvmat.entity.enums.VolunteerStavEnum;

import java.util.List;
import java.util.UUID;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, UUID> {


    @Query("select v from VolunteerEntity v where not(v.emailSend) and v.stav != 'ZRUSENY' and v.eventId = :eventId")
    List<VolunteerEntity> findEmailNotSend();

    @Query("""
                select v from VolunteerEntity v
                where (:stavy is null or v.stav in :stavy)
                  and v.name like %:search%
                  and v.eventId = :eventId
            """)
    List<VolunteerEntity> findAllByEventIdNameAndStav(UUID eventId, String search, List<VolunteerStavEnum> stavy);
}
