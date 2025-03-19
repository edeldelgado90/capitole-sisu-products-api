CREATE TABLE brands
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    photo       VARCHAR(255)
);

CREATE TABLE price_list
(
    id          BIGINT PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE prices
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id   BIGINT         NOT NULL,
    start_date TIMESTAMP      NOT NULL,
    end_date   TIMESTAMP      NOT NULL,
    price_list BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    priority   INT            NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    curr       VARCHAR(3)     NOT NULL,

    FOREIGN KEY (brand_id) REFERENCES brands (id),
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (price_list) REFERENCES price_list (id),

    CONSTRAINT unq_price UNIQUE (brand_id, product_id, curr, price_list, priority)

);

CREATE INDEX idx_price_brand_product ON prices (brand_id, product_id);
CREATE INDEX idx_price_start_end ON prices (start_date, end_date);
