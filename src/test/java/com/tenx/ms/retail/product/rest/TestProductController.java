package com.tenx.ms.retail.product.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.BaseTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.product.rest.dto.Product;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RetailServiceApp.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class TestProductController extends BaseTest {
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

    @Value("classpath:product/product_failure_name.json")
    private File productFailedName;

    @Value("classpath:product/product_failure_description.json")
    private File productFailedDescription;

    @Value("classpath:product/product_failure_price.json")
    private File productFailedPrice;

    @Value("classpath:product/product_failure_sku.json")
    private File productFailedSku;

    @Value("classpath:stock/stock_success.json")
    private File stockSuccess;

    protected static final Long STORE_ID_1 = 1L;
    protected static final Long PRODUCT_ID_1 = 1L;


    @Test
    @FlywayTest
    public void testCreateProductSuccess() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productSuccess);
        Long received =  mapper.readValue(resourceCreated.getBody(), new TypeReference<Long>() {});
        assertEquals("HTTP Status code incorrect", HttpStatus.CREATED, resourceCreated.getStatusCode());
        assertEquals(received, PRODUCT_ID_1);

    }

    @Test
    @FlywayTest
    public void testCreateProductFailNoName() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedName);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }

    @Test
    @FlywayTest
    public void testCreateProductFailNoSku() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedSku);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }
    @Test
    @FlywayTest
    public void testCreateProductFailShortSku() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedSku);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }
    @Test
    @FlywayTest
    public void testCreateProductFailNotAlphaSku() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedSku);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }


    @Test
    @FlywayTest
    public void testCreateProductFailNoDescription() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedDescription);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }
    @Test
    @FlywayTest
    public void testCreateProductFailNoPrice() throws IOException {
        createStore(storeSuccess);
        ResponseEntity<String> resourceCreated = createProduct(STORE_ID_1, productFailedPrice);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());

    }


    @Test
    @FlywayTest
    public void testGetProductsById() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        Integer id = 1;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_PRODUCT, getBasePath(),id), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        Product product = new Product();
        product.setName("product");
        product.setProductId(PRODUCT_ID_1);
        product.setDescription("description1");
        product.setPrice(new BigDecimal("1.10"));
        product.setSku("sku11");
        List<Product> products = mapper.readValue(response.getBody(), new TypeReference<List<Product>>(){});
        assertEquals("Incorrect product retrieved",product, products.get(0));
    }


    @Test
    @FlywayTest
    public void testGetProductsByIdNoProducts() throws IOException {
        createStore(storeSuccess);
        Integer id = 3;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_PRODUCT, getBasePath(),id), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        List<Product> product2 = mapper.readValue(response.getBody(), new TypeReference<List<Product>>(){});
        assertEquals("Product list not empty", product2, new ArrayList<Product>());
    }

    @Test
    @FlywayTest
    public void testGetProductById() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        Integer id = 1;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_PRODUCT_WITH_PRODUCT, getBasePath(),id,id), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        Product product = new Product();
        product.setName("product");
        product.setProductId(PRODUCT_ID_1);
        product.setDescription("description1");
        product.setPrice(new BigDecimal("1.10"));
        product.setSku("sku11");
        Product productReturned = mapper.readValue(response.getBody(), new TypeReference<Product>(){});
        assertEquals("Incorrect product retrieved", product, productReturned);
    }

    @Test
    @FlywayTest
    public void testGetProductByIdNoProduct() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        Integer storeId = 1;
        Integer productId = 3;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_PRODUCT_WITH_PRODUCT, getBasePath(),storeId,productId), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
