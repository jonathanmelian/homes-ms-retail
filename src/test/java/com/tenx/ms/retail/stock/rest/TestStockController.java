package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.BaseTest;
import com.tenx.ms.retail.RetailServiceApp;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestStockController extends BaseTest {
    protected static final String API_VERSION = RestConstants.VERSION_ONE;
    protected static final String REQUEST_URI = "%s"+ API_VERSION + "/stores/";
    protected static final String REQUEST_URI_PRODUCT = "%s"+ API_VERSION + "/products/%s";
    protected static final String REQUEST_URI_PRODUCT_WITH_PRODUCT = "%s"+ API_VERSION + "/products/%s/%s";



    @Value("classpath:store/store_success.json")
    private File storeSuccess;

    @Value("classpath:product/product_success.json")
    private File productSuccess;

    @Value("classpath:product/product_success2.json")
    private File productSuccess2;

    @Value("classpath:stock/stock_success.json")
    private File stockSuccess;

    @Value("classpath:stock/stock_failure.json")
    private File stockFailure;

    protected static final Long STORE_ID_1 = 1L;
    protected static final Long STORE_ID_2 = 2L;
    protected static final Long PRODUCT_ID_1 = 1L;
    protected static final Long PRODUCT_ID_2 = 2L;


    @Test
    @FlywayTest
    public void testCreateStockSuccess() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        ResponseEntity<String> resourceCreated = createStock(STORE_ID_1, PRODUCT_ID_1, stockSuccess);
        assertEquals("HTTP Status code incorrect", HttpStatus.OK, resourceCreated.getStatusCode());

    }

    @Test
    @FlywayTest
    public void testCreateStockFailStoreNotFound() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        ResponseEntity<String> resourceCreated = createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess);
        assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, resourceCreated.getStatusCode());

    }

    @Test
    @FlywayTest
    public void testCreateStockFailProductNotFound() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        ResponseEntity<String> resourceCreated = createStock(STORE_ID_1, PRODUCT_ID_2, stockSuccess);
        assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, resourceCreated.getStatusCode());

    }

    @Test
    @FlywayTest
    public void testCreateStockFailProductNoCount() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        ResponseEntity<String> resourceCreated = createStock(STORE_ID_1, PRODUCT_ID_1, stockFailure);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }
}
