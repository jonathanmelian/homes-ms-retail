package com.tenx.ms.retail.product.domain;

import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(targetEntity = StoreEntity.class)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private StoreEntity store;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "sku", nullable = false, length = 255)
    private String sku;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "product")
    private StockEntity stock;

}
