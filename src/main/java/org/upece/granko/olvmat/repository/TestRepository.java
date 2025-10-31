package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

}
