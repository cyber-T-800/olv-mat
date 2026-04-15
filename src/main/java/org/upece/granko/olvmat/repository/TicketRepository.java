package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.TicketEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    @Query("select count(*) from TicketEntity t where (t.typListka = 'STUDENT' or t.typListka = 'NESTUDENT') and t.stav != 'ZRUSENY' and t.event.id=:eventID")
    int countUcastnicke(UUID eventID);

    @Query("select count(*) from TicketEntity t where (t.typListka = 'DOBROVOLNIK') and t.stav != 'ZRUSENY' and t.event.id=:eventID")
    int countDobrovolnicke(UUID eventID);

    @Query("select count(*) from TicketEntity t where (t.typListka = 'TEAM') and t.stav != 'ZRUSENY' and t.event.id=:eventID")
    int countTeamacke(UUID eventID);

    @Query("select count(*) from TicketEntity t where  t.stav != 'ZRUSENY' and t.event.id=:eventID")
    int countAll(UUID eventID);

    @Query("select count(*) from TicketEntity t where (t.stav = 'ZAPLATENY' or t.stav = 'POUZITY') and t.event.id=:eventID")
    int countZaplatene(UUID eventID);

    @Query("select count(*) from TicketEntity t where t.stav = 'POUZITY' and t.event.id=:eventID")
    int countPouzite(UUID eventID);

    @Query("select t from TicketEntity t where t.stav != 'ZRUSENY' and t.event.id=:eventID")
    List<TicketEntity> findAllExceptZrusene(UUID eventID);

    @Query("select count(t) > 0 from TicketEntity t where t.stav != 'ZRUSENY' and (t.email = :email and t.meno = :meno) and t.event.id=:eventID")
    Boolean findDuplicity(
            String email,
            String meno,
            UUID eventID
    );

    @Query("select t from TicketEntity t where t.event.id=:eventID")
    List<TicketEntity> findAllByEventId(UUID eventID);
}
