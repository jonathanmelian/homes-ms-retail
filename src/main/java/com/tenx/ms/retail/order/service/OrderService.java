package com.tenx.ms.retail.order.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.enums.OrderStatus;
import com.tenx.ms.retail.order.repositories.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.order.rest.dto.OrderResult;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.stock.service.StockService;
import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final static EntityConverter<Order, OrderEntity> ORDER_CONVERTER = new EntityConverter<>(Order.class, OrderEntity.class);
    private final static EntityConverter<OrderItem, OrderItemEntity> ITEMS_CONVERTER = new EntityConverter<>(OrderItem.class, OrderItemEntity.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;

    @Transactional
    public OrderResult create(Long storeId, Order order){
        List<OrderItem> backOrderedItems = new ArrayList<>();
        List<OrderItemEntity> approvedItems = new ArrayList<>();
        OrderResult orderResult = new OrderResult();
        OrderItemEntity orderItemEntity;
        for(OrderItem orderItem : order.getItems()){
            if (!stockService.updateStock(storeId, orderItem.getProductId(), orderItem.getCount())){
                backOrderedItems.add(orderItem);
            }else{
                orderItemEntity = ITEMS_CONVERTER.toT2(orderItem);
                ProductEntity productEntity = new ProductEntity();
                productEntity.setProductId(orderItem.getProductId());
                orderItemEntity.setProduct(productEntity);
                approvedItems.add(orderItemEntity);
            }
        }
        orderResult.setItems(backOrderedItems);
        if (!approvedItems.isEmpty() ) {
            OrderEntity oe = ORDER_CONVERTER.toT2(order);
            oe.setItems(approvedItems);
            StoreEntity se = new StoreEntity();
            se.setStoreId(storeId);
            oe.setStore(se);
            oe.setStatus(OrderStatus.ORDERED);
            orderResult.setOrderStatus(OrderStatus.ORDERED);
            orderResult.setId(orderRepository.save(oe).getOrderId());

        }else{
            orderResult.setOrderStatus(OrderStatus.FAILED);
        }

        return orderResult;
    }

}
