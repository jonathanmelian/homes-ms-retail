package com.tenx.ms.retail.order.domain;

import com.tenx.ms.retail.product.domain.ProductEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="retail_order_item")
public class OrderItemEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @OneToOne(targetEntity = ProductEntity.class)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name = "order_id", insertable = false, updatable = false, referencedColumnName = "order_id", nullable = false)
    private OrderEntity order;

    @ApiModelProperty(value="Product count", required = true)
    @Column(name = "count", nullable = false)
    private Integer count;

}
