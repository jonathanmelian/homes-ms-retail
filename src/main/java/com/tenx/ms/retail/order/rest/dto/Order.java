package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import com.tenx.ms.retail.order.enums.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    @ApiModelProperty(value = "Order id", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Order date")
    private LocalDateTime orderDate;

    @ApiModelProperty(value = "Order status")
    private OrderStatus orderStatus;

    @ApiModelProperty(value = "Order items")
    @NotNull
    private List<OrderItem> items;

    @ApiModelProperty(value = "First name of the buyer")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only alpha characters")
    @Valid
    @NotEmpty
    @Size(max = 35)
    private String firstName;

    @ApiModelProperty(value = "Last name of the buyer")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only alpha characters")
    @Valid
    @NotEmpty
    @Size(max = 35)
    private String lastName;

    @ApiModelProperty(value = "Email of the buyer")
    @Email
    @NotNull
    private String email;

    @ApiModelProperty(value = "Phone number of the buyer")
    @PhoneNumber
    @NotNull
    private String phone;


}
