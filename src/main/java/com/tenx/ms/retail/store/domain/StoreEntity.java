package com.tenx.ms.retail.store.domain;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<ProductEntity> products;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<StockEntity> stocks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<OrderEntity> orders;

}
