package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.TicketEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    @Query("select count(*) from TicketEntity t where (t.typListka = 'STUDENT' or t.typListka = 'NESTUDENT') and t.stav != 'ZRUSENY'")
    int countUcastnicke();

    @Query("select count(*) from TicketEntity t where (t.typListka = 'DOBROVOLNIK') and t.stav != 'ZRUSENY'")
    int countDobrovolnicke();

    @Query("select count(*) from TicketEntity t where (t.typListka = 'TEAM') and t.stav != 'ZRUSENY'")
    int countTeamacke();

    @Query("select t from TicketEntity t where t.stav != 'ZRUSENY'")
    List<TicketEntity> findAllExceptZrusene();
}
