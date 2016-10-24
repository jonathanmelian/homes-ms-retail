package com.tenx.ms.retail.store.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repositories.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final static EntityConverter<Store, StoreEntity> CONVERTER = new EntityConverter<>(Store.class, StoreEntity.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductService productService;

    public Long create(Store store) {
        StoreEntity entity = storeRepository.save(CONVERTER.toT2(store));
        return entity.getStoreId();
    }

    public Store getStoreById(Long id) {
        Optional<Store> store = storeRepository.findOneByStoreId(id).map(CONVERTER::toT1);
        if(store.isPresent()){
            return store.get();
        }else{
            throw new NoSuchElementException("Store not found");
        }
    }

    public List<Store> getAllStores(Optional name) {
        List<StoreEntity> entities;
        if(name.isPresent()){
            entities = storeRepository.findByNameContainingIgnoreCase((String)name.get());
        }else{
            entities = storeRepository.findAll();
        }
        return entities.stream().map(CONVERTER::toT1).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long storeId) throws NoSuchElementException {
        Optional<Store> store = storeRepository.findOneByStoreId(storeId).map(CONVERTER::toT1);
        if (store.isPresent()) {
            storeRepository.delete(storeId);
        }
    }
}
