package com.tenx.ms.retail.product.rest.dto;


import com.tenx.ms.commons.validation.constraints.DollarAmount;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class Product {

    @ApiModelProperty(value = "Product Id", required = true)
    private Long productId;

    @ApiModelProperty(value = "Store Id", required = true)
    private Long storeId;

    @ApiModelProperty(value = "Product name", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "Product description", required = true)
    @NotNull
    private String description;

    @ApiModelProperty(value = "Stock-keeping unit", required = true)
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{5,10}$", message = "The SKU must be alphanumeric")
    private String sku;

    @ApiModelProperty(value = "Product price", required = true)
    @NotNull
    @DollarAmount
    private BigDecimal price;
}
