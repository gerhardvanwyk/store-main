-- Create customer table
CREATE TABLE customer (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- Create order table
CREATE TABLE "order" (
                         id BIGSERIAL PRIMARY KEY,
                         description VARCHAR(255) NOT NULL
);

-- Create product table
CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

-- Create order_product join table
CREATE TABLE order_product
(
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_product_order FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Create order_customer join table
CREATE TABLE order_customer
(
    order_id    BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, customer_id),
    CONSTRAINT fk_order_customer_order FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT fk_order_customer_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);
