package com.tenx.ms.retail.stock.domain;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "stock")
public class StockEntity {

    @Id
    @Column(name = "product_id", insertable = false, updatable = false, nullable = false)
    private Long stockId;

    @OneToOne(targetEntity = ProductEntity.class)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity product;

    @ManyToOne(targetEntity = StoreEntity.class)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private StoreEntity store;

    @Column(name = "count", nullable = false)
    private Integer count;

}
