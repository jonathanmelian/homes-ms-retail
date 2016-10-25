package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "products", description = "Product Management API")
@RestController
@RequestMapping(RestConstants.VERSION_ONE + "/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Creates or updates an stock")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Success"),
        @ApiResponse(code = 412, message = "Precondition failure"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value="/{storeId:\\d+}/{productId:\\d+}", method = { RequestMethod.PUT, RequestMethod.POST })
    public void upsert(
        @ApiParam(name = "Store id", value="The id of the store", required = true) @PathVariable Long storeId,
        @ApiParam(name = "Product id", value = "The id of the product to get", required = true) @PathVariable Long productId,
        @ApiParam(name = "stock", value = "Stock item", required = true) @Validated @RequestBody Stock stock) {
        stockService.saveStock(storeId, productId, stock);
    }

    @ApiOperation(value = "Gets the stock count for a product and a store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value="/{storeId:\\d+}/{productId:\\d+}", method = { RequestMethod.GET})
    public Integer getStock(
        @ApiParam(name = "Store id", value="The id of the store", required = true) @PathVariable Long storeId,
        @ApiParam(name = "Product id", value = "The id of the product to get", required = true) @PathVariable Long productId){
        return stockService.getStock(storeId, productId);
    }
}
