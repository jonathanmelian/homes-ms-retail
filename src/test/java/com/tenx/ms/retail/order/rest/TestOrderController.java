package com.tenx.ms.retail.order.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.BaseTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.order.rest.dto.OrderResult;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestOrderController extends BaseTest {
    protected static final String API_VERSION = RestConstants.VERSION_ONE;
    protected static final String REQUEST_URI = "%s"+ API_VERSION + "/stores/";
    protected static final String REQUEST_URI_PRODUCT = "%s"+ API_VERSION + "/products/%s";
    protected static final String REQUEST_URI_STOCK = "%s"+ API_VERSION + "/stock/%s/%s";
    protected static final String REQUEST_URI_ORDER = "%s"+ API_VERSION + "/orders/%s";



    @Value("classpath:store/store_success.json")
    private File storeSuccess;

    @Value("classpath:product/product_success.json")
    private File productSuccess;

    @Value("classpath:product/product_success2.json")
    private File productSuccess2;

    @Value("classpath:stock/stock_success.json")
    private File stockSuccess;

    @Value("classpath:stock/stock_success2.json")
    private File stockSuccess2;

    @Value("classpath:stock/stock_failure.json")
    private File stockFailure;

    @Value("classpath:order/order_success.json")
    private File orderSuccess;

    @Value("classpath:order/order_fail_no_first_name.json")
    private File orderFailure;

    @Value("classpath:order/order_fail_no_last_name.json")
    private File orderFailure2;

    @Value("classpath:order/order_fail_invalid_phone.json")
    private File orderFailure3;

    @Value("classpath:order/order_fail_invalid_no_phone.json")
    private File orderFailure4;

    @Value("classpath:order/order_fail_invalid_email.json")
    private File orderFailure5;

    @Value("classpath:order/order_fail_no_email.json")
    private File orderFailure6;


    protected static final Long STORE_ID_1 = 1L;
    protected static final Long PRODUCT_ID_1 = 1L;
    protected static final Long PRODUCT_ID_2 = 2L;
    protected static final Long ORDER_ID_1 = 1L;

    @Test
    @FlywayTest
    public void testCreateOrderSuccess() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderSuccess), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.CREATED, response.getStatusCode());
        OrderResult orderResult = mapper.readValue(response.getBody(), new TypeReference<OrderResult>(){});
        OrderItem orderItemReturned = new OrderItem();
        orderItemReturned.setCount(10);
        orderItemReturned.setProductId(PRODUCT_ID_2);
        List<OrderItem> orderItemsReturned = new ArrayList<OrderItem>();
        orderItemsReturned.add(orderItemReturned);
        assertEquals("Order id does not match", orderResult.getId() ,ORDER_ID_1);
        assertEquals("Returned items do not match", orderResult.getItems(), orderItemsReturned);
        ResponseEntity<String> stockResponse = getStringResponse(String.format(REQUEST_URI_STOCK, getBasePath(),STORE_ID_1,PRODUCT_ID_1), null, HttpMethod.GET);
        Integer stock = mapper.readValue(stockResponse.getBody(), new TypeReference<Integer>(){});
        assertEquals("Stock not updated", stock, new Integer(4) );
    }

    @Test
    @FlywayTest
    public void testCreateOrderFailNoFirstName() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testCreateOrderFailNoLastName() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure2), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testCreateOrderFailNoPhone() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure3), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testCreateOrderFailInvalidPhone() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure4), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }
    @Test
    @FlywayTest
    public void testCreateOrderFailInvalidEmail() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure5), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testCreateOrderFailNoEmail() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(),STORE_ID_1), FileUtils.readFileToString(orderFailure6), HttpMethod.POST);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
    }



}
