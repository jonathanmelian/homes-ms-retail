package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderResult;
import com.tenx.ms.retail.order.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "products", description = "Product Management API")
@RestController
@RequestMapping(RestConstants.VERSION_ONE + "/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Creates an order")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Success"),
        @ApiResponse(code = 412, message = "Precondition Failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResult> createProduct(
        @ApiParam(name = "Store id", value="The id of the store", required = true) @PathVariable("storeId") Long storeId,
        @ApiParam(name = "Order", value="The order to create", required = true) @Validated @RequestBody Order order) {
        return new ResponseEntity<>(orderService.create(storeId, order), HttpStatus.CREATED);
    }
}
