package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class OrderItem {
    @ApiModelProperty(value = "Product Id", required = true)
    private Long productId;

    @ApiModelProperty(value = "Product count", required = true)
    @Min(value = 1)
    private Integer count;
}
