package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.retail.order.enums.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class OrderResult {
    @ApiModelProperty(value = "Order id identified by a number", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "Order status")
    private OrderStatus orderStatus;

    @ApiModelProperty(value = "Order items")
    private List<OrderItem> items;

}
