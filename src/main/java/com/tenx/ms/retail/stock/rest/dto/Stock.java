package com.tenx.ms.retail.stock.rest.dto;

import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.rest.dto.Store;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Stock {

    @ApiModelProperty(value = "Store Id", required = true)
    private Store storeId;

    @ApiModelProperty(value = "Product Id", required = true)
    private Product productId;

    @ApiModelProperty(value="Stock count", required = true)
    @NotNull
    private Integer count;

}
