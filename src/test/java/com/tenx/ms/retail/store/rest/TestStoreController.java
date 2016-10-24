package com.tenx.ms.retail.store.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.BaseTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.store.rest.dto.Store;
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
public class TestStoreController extends BaseTest {
    protected static final String API_VERSION = RestConstants.VERSION_ONE;
    protected static final String REQUEST_URI = "%s"+ API_VERSION + "/stores/";
    protected static final String REQUEST_URI_WITH_NAME = "%s"+ API_VERSION + "/stores/%s";


    @Value("classpath:store/store_success.json")
    private File storeSuccess;

    @Value("classpath:store/store_success2.json")
    private File storeSuccess2;

    @Value("classpath:store/store_failure.json")
    private File storeFailure;

    @Value("classpath:product/product_success.json")
    private File productSuccess;

    protected static final String REQUEST_URI_PRODUCT_WITH_PRODUCT = "%s"+ API_VERSION + "/products/%s/%s";
    protected static final Long STORE_ID_1 = 1L;
    protected static final Long PRODUCT_ID_1 = 1L;


    @Test
    @FlywayTest
    public void testCreateStoreSuccess() throws IOException {
        ResponseEntity<String> resourceCreated = createStore(storeSuccess);
        Long received =  mapper.readValue(resourceCreated.getBody(), new TypeReference<Long>() {});
        assertEquals("HTTP Status code incorrect", HttpStatus.CREATED, resourceCreated.getStatusCode());
        assertEquals(received, STORE_ID_1);
    }

    @Test
    @FlywayTest
    public void testCreateStoreFail() throws IOException {
        ResponseEntity<String> resourceCreated = createStore(storeFailure);
        assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, resourceCreated.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testGetStoreByName() throws IOException {
        createStore(storeSuccess);
        createStore(storeSuccess2);
        String nameToSearch = "?name=name2";
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_WITH_NAME, getBasePath(),nameToSearch), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        Store store = new Store();
        store.setStoreId(2L);
        store.setName("store name2");
        List<Store> store2 = mapper.readValue(response.getBody(), new TypeReference<List<Store>>(){});
        assertEquals("Incorrect store retrieved", store2.get(0), store);
    }


    @Test
    @FlywayTest
    public void testGetStoreByNameEmpty() throws IOException {
        createStore(storeSuccess);
        createStore(storeSuccess2);
        String nameToSearch = "?name=name3";
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_WITH_NAME, getBasePath(),nameToSearch), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        List<Store> store2 = mapper.readValue(response.getBody(), new TypeReference<List<Store>>(){});
        assertEquals("Store list not empty",store2, new ArrayList<Store>());
    }

    @Test
    @FlywayTest
    public void testGetStoreById() throws IOException {
        createStore(storeSuccess);
        createStore(storeSuccess2);
        Integer id = 1;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_WITH_NAME, getBasePath(),id), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        Store store = new Store();
        store.setStoreId(1L);
        store.setName("store name");
        Store store2 = mapper.readValue(response.getBody(), new TypeReference<Store>(){});
        assertEquals("Incorrect store retrieved", store2, store);
    }


    @Test
    @FlywayTest
    public void testGetStoreByIdFail() throws IOException {
        createStore(storeSuccess);
        createStore(storeSuccess2);
        Integer id = 3;
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_WITH_NAME, getBasePath(),id), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testGetStores() throws IOException {
        createStore(storeSuccess);
        createStore(storeSuccess2);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI, getBasePath()), null, HttpMethod.GET );
        assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
        List<Store> storeList = mapper.readValue(response.getBody(), new TypeReference<List<Store>>(){});
        Store store = new Store();
        store.setName("store name");
        store.setStoreId(1L);
        Store store2 = new Store();
        store2.setName("store name2");
        store2.setStoreId(2L);
        List<Store> expectedList = new ArrayList<>();
        expectedList.add(store);
        expectedList.add(store2);
        assertEquals("Incorrect product list", storeList, expectedList );
    }

    @Test
    @FlywayTest
    public void testDeleteStoreSuccess() throws IOException {
        createStore(storeSuccess);
        createProduct(STORE_ID_1, productSuccess);
        createStock(STORE_ID_1, PRODUCT_ID_1, storeSuccess);
        ResponseEntity<String> response = getStringResponse(String.format(REQUEST_URI_WITH_NAME, getBasePath(),STORE_ID_1), null, HttpMethod.DELETE );
        assertEquals("HTTP Status code incorrect", HttpStatus.NO_CONTENT, response.getStatusCode());
        ResponseEntity<String> productResponse = getStringResponse(String.format(REQUEST_URI_PRODUCT_WITH_PRODUCT, getBasePath(),STORE_ID_1,PRODUCT_ID_1), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, productResponse.getStatusCode());
        ResponseEntity<String> stockResponse = getStringResponse(String.format(REQUEST_URI_STOCK, getBasePath(),STORE_ID_1,PRODUCT_ID_1), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, stockResponse.getStatusCode());
    }
}
