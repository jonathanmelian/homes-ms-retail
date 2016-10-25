package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "products", description = "Product Management API")
@RestController
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Creates a product")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Success"),
        @ApiResponse(code = 412, message = "Precondition Failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("!authentication.isClientOnly()")
    public ResponseEntity<Long> createProduct(
        @ApiParam(name = "Store id", value="The id of the store", required = true) @PathVariable("storeId") Long storeId,
        @ApiParam(name = "Product", value="The product to create", required = true) @Validated @RequestBody Product product) {
        return new ResponseEntity<>(productService.create(storeId, product), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets a product by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Product not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}/{productId:\\d+}"}, method = RequestMethod.GET)
    public Product getProductById(
        @ApiParam(name = "Store id", value = "The id of the store to get", required = true)@PathVariable Long storeId,
        @ApiParam(name = "Product id", value = "The id of the product to get", required = true)@PathVariable Long productId){
        return productService.getProductByIdAndStoreId(storeId, productId);
    }

    @ApiOperation(value = "List of Products for a store id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"},method = RequestMethod.GET)
    public List<Product> getAllProductsByStoreId(
        @ApiParam(name = "Store id", value = "The id of the store Of the products", required = true)@PathVariable Long storeId){
        return productService.getAllProductsByStoreId(storeId);
    }


}
