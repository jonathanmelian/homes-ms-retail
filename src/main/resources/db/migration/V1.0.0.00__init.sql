CREATE TABLE store (
    store_id        BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'Store id',
    name            VARCHAR(100)   NOT NULL                COMMENT 'Store name',
    PRIMARY KEY (store_id)
);
CREATE TABLE product (
    product_id      BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'Product id',
    store_id        BIGINT         NOT NULL                COMMENT 'Store id',
    name            VARCHAR(100)   NOT NULL                COMMENT 'Product name',
    description     VARCHAR(100)   NOT NULL                COMMENT 'Description of the product',
    sku             VARCHAR(10)    NOT NULL                COMMENT 'Product SKU',
    price           DECIMAL(20, 2) NOT NULL                COMMENT 'Product price',
    FOREIGN KEY (store_id) REFERENCES store (store_id),
    PRIMARY KEY (product_id)
);
CREATE TABLE  stock (
    product_id      BIGINT          NOT NULL               COMMENT 'Product id',
    store_id        BIGINT          NOT NULL               COMMENT 'Store id',
    count           INT             NOT NULL               COMMENT 'Stock count',
    PRIMARY KEY (product_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);
CREATE TABLE retail_order (
    order_id        BIGINT          NOT NULL AUTO_INCREMENT                COMMENT 'Order id',
    store_id        BIGINT          NOT NULL                               COMMENT 'Store id',
    order_date      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT 'Order date',
    status          INT                                                    COMMENT 'Status of the order (up to 10 characters)',
    first_name      VARCHAR(50)                                            COMMENT 'First Name of the purchaser of the order (up to 50 characters)',
    last_name       VARCHAR(50)                                            COMMENT 'Last name of the purchaser of the order (up to 50 characters)',
    email           VARCHAR(255)                                           COMMENT 'Email of the purchaser of the order (up to 255 characters)',
    phone           VARCHAR(25)                                            COMMENT 'Phone number of the of the order',
    PRIMARY KEY (order_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE retail_order_item (
    order_item_id   BIGINT NOT NULL AUTO_INCREMENT      COMMENT 'Id of the order item',
    order_id           BIGINT NOT NULL                     COMMENT 'Order id referencing the retail order table',
    product_id         BIGINT NOT NULL                     COMMENT 'Product id referencing the product id',
    count              INT    NOT NULL                     COMMENT 'Amount of products purchased',
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES retail_order(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
