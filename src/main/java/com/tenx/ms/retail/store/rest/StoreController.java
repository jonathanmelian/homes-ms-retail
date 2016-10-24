package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "stores", description = "Store Management API")
@RestController
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "Creates a store")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Success"),
        @ApiResponse(code = 412, message = "Precondition Failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("!authentication.isClientOnly()")
    public ResponseEntity<Long> createStore(
        @ApiParam(name = "Store", value="The store to be created", required = true) @Validated @RequestBody Store store) {
        return new ResponseEntity<>(storeService.create(store), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets a store by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404, message = "Store not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.GET)
    public Store getStoreById(
        @ApiParam(name = "Store id", value = "The id of the store to get", required = true) @PathVariable Long storeId){
        return storeService.getStoreById(storeId);
    }

    @ApiOperation(value = "Lists all stores")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(method = RequestMethod.GET)
    public List<Store> getAllStores(
        @ApiParam(name = "Name", value="A string included in the products name") @RequestParam(value = "name", required = false) Optional name){
        return storeService.getAllStores(name);
    }
    @ApiOperation(value = "Deletes a store by id")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Success"),
        @ApiResponse(code = 404, message = "Store not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{storeId:\\d+}", method = RequestMethod.DELETE)
    public void delete(
        @ApiParam(name = "Store id", value = "The id of the store to delete", required = true) @PathVariable("storeId") Long storeId) {
        storeService.delete(storeId);
    }
}
