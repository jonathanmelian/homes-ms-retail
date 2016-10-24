package com.tenx.ms.retail.store.rest.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Store {

    @ApiModelProperty(value = "Store Id", required = true)
    private Long storeId;

    @ApiModelProperty(value="Store name", required = true)
    @NotNull
    private String name;
}
