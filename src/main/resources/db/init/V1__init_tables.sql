CREATE TABLE category
(
    name          VARCHAR(255) PRIMARY KEY,
    markup_percentage DECIMAL
);

CREATE TABLE goods
(
    name        VARCHAR(255) PRIMARY KEY,
    category_name VARCHAR(255),
    price       DECIMAL,
    balance     BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE orders_goods
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    good_name      VARCHAR(255),
    order_id       BIGINT,
    goods_quantity BIGINT
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name     VARCHAR(255),
    surname  VARCHAR(255),
    position VARCHAR(255),
    email    VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE companies
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name    VARCHAR(255),
    address VARCHAR(255),
    phone   VARCHAR(255),
    email   VARCHAR(255)
);

CREATE TABLE orders
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    user_id    BIGINT,
    company_id BIGINT,
    created_at DATE
);

ALTER TABLE goods
    ADD CONSTRAINT fk_goods_on_category FOREIGN KEY (category_name) REFERENCES category (name);
ALTER TABLE orders_goods
    ADD CONSTRAINT fk_orders_goods_on_good_name FOREIGN KEY (good_name) REFERENCES goods (name);
ALTER TABLE orders_goods
    ADD CONSTRAINT fk_orders_goods_on_order_id FOREIGN KEY (order_id) REFERENCES orders (id);