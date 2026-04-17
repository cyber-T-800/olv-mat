package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.VolunteerEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, UUID> {

    @Override
    @Query("select v from VolunteerEntity v where v.stav != 'ZRUSENY'")
    List<VolunteerEntity> findAll();

    @Query("select v from VolunteerEntity v where not(v.emailSend) and v.stav != 'ZRUSENY'")
    List<VolunteerEntity> findEmailNotSend();
}
