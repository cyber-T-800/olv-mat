package org.upece.granko.olvmat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByEventAndType(EventEntity event, String type);
}
