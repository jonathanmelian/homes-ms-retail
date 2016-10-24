package com.tenx.ms.retail.product.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repositories.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.stock.service.StockService;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService{
    private final static EntityConverter<Product, ProductEntity> CONVERTER = new EntityConverter<>(Product.class, ProductEntity.class);


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StockService stockService;

    public Long create(Long storeId, Product product) {
        StoreEntity se = storeRepository.findOne(storeId);
        ProductEntity pe = CONVERTER.toT2(product);
        pe.setStore(se);
        return productRepository.save(pe).getProductId();
    }

    public Product getProductByIdAndStoreId(Long storeId, Long productId) {
        Optional productEntity = productRepository.findByProductIdAndStoreStoreId(productId, storeId);
        if (productEntity.isPresent()) {
            return CONVERTER.toT1((ProductEntity) productEntity.get());
        }else {
            throw new NoSuchElementException();
        }
    }

    public List<Product> getAllProductsByStoreId(Long storeId) {
        List<ProductEntity> entities = productRepository.findByStoreStoreId(storeId);
        return entities.stream().map(CONVERTER::toT1).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long storeId, Long productId) {
        stockService.delete(storeId, productId);
        Optional productEntity = productRepository.findByProductIdAndStoreStoreId(productId, storeId);
        if (productEntity.isPresent()) {
            productRepository.delete(productId);
        }
    }

}
