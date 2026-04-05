package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.AdminEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, UUID> {

    Optional<AdminEntity> findByEmail(String email);

    @Query("select count(a)=0 from AdminEntity a where a.rola='SUPERADMIN'")
    boolean vacantSuperadminPosition();
}
