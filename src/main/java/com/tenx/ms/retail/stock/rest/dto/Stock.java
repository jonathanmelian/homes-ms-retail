package com.tenx.ms.retail.stock.rest.dto;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Stock {

    @ApiModelProperty(value = "Store Id", required = true)
    private StoreEntity storeId;

    @ApiModelProperty(value = "Product Id", required = true)
    private ProductEntity productId;

    @ApiModelProperty(value="Stock count", required = true)
    @NotNull
    private Integer count;

}
