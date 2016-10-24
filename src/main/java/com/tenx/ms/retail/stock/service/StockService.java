package com.tenx.ms.retail.stock.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repositories.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repositories.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockService {
    private final static EntityConverter<Stock, StockEntity> CONVERTER = new EntityConverter<>(Stock.class, StockEntity.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StockRepository stockRepository;


    @Transactional
    public void saveStock(Long storeId, Long productId, Stock stock) {
        StoreEntity se = storeRepository.findOne(storeId);
        ProductEntity pe = productRepository.findOne(productId);
        if(se == null || pe == null || !pe.getStore().getStoreId().equals(storeId)){
            throw new NoSuchElementException();
        }
        StockEntity stockEntity = CONVERTER.toT2(stock);
        stockEntity.setStockId(productId);
        stockEntity.setStore(se);
        stockEntity.setProduct(pe);
        stockRepository.save(stockEntity);
    }

    public boolean updateStock(Long storeId, Long productId, Integer count){
        Optional<StockEntity> se = stockRepository.findByStoreStoreIdAndProductProductId(storeId,productId);
        if(se.isPresent() && count <= se.get().getCount() ){
            se.get().setCount(se.get().getCount() - count);
            stockRepository.save(se.get());
            return true;
        }else{
            return false;
        }
    }

    public Integer getStock(Long storeId, Long productId) {
        {
            Optional<StockEntity> stock = stockRepository.findByStoreStoreIdAndProductProductId(storeId, productId);
            if (stock.isPresent()) {
                return stock.get().getCount();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public void delete(Long storeId, Long productId) {
        Optional<StockEntity> se = stockRepository.findByStoreStoreIdAndProductProductId(storeId,productId);
        if (se.isPresent()) {
            stockRepository.delete(productId);
        }
    }

}
