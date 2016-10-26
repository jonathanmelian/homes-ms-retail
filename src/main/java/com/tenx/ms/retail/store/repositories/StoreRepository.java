package com.tenx.ms.retail.store.repositories;

import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    List<StoreEntity> findByNameContainingIgnoreCase(final String name);
    Optional<StoreEntity> findOneByStoreId(Long id);
}
