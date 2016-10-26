package com.tenx.ms.retail.order.domain;

import com.tenx.ms.retail.order.enums.OrderStatus;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "retail_order")
public class OrderEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne(targetEntity = StoreEntity.class)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private StoreEntity store;

    @Column(name = "status", nullable = false, length = 255)
    private OrderStatus status;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "order_date", insertable = false, nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 255)
    private String phone;

    @OneToMany(targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name= "order_id", nullable = false)
    private List<OrderItemEntity> items;
}
