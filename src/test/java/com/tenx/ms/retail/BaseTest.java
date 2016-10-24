package com.tenx.ms.retail;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.tests.BaseIntegrationTest;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

import static org.aspectj.bridge.MessageUtil.fail;

public class BaseTest extends BaseIntegrationTest{
    protected static final String API_VERSION = RestConstants.VERSION_ONE;
    protected static final String REQUEST_URI_STORE = "%s"+ API_VERSION + "/stores/";
    protected static final String REQUEST_URI_PRODUCT = "%s"+ API_VERSION + "/products/%s";
    protected static final String REQUEST_URI_STOCK = "%s"+ API_VERSION + "/stock/%s/%s";
    protected static final String REQUEST_URI_ORDER = "%s"+ API_VERSION + "/order/%s";


    public ResponseEntity<String> createStore(File file) {
        try {
            return getStringResponse(String.format(REQUEST_URI_STORE, getBasePath()), FileUtils.readFileToString(file), HttpMethod.POST);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<String> createProduct(Long storeId, File file) {
        try {
            return getStringResponse(String.format(REQUEST_URI_PRODUCT, getBasePath(), storeId), FileUtils.readFileToString(file), HttpMethod.POST);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<String> createStock(Long storeId, Long productId, File file) {
        try {
            return getStringResponse(String.format(REQUEST_URI_STOCK, getBasePath(), storeId, productId), FileUtils.readFileToString(file), HttpMethod.POST);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<String> createOrder(Long storeId, File file) {
        try {
            return getStringResponse(String.format(REQUEST_URI_ORDER, getBasePath(), storeId), FileUtils.readFileToString(file), HttpMethod.POST);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

}
