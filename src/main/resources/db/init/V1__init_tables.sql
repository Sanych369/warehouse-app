CREATE TABLE category
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name              VARCHAR(255)                            NOT NULL UNIQUE,
    markup_percentage DECIMAL DEFAULT 0
);

CREATE TABLE goods
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name           VARCHAR(255)                            NOT NULL UNIQUE,
    category_id    BIGINT                                  NOT NULL,
    sale_price     BIGINT                                  NOT NULL,
    purchase_price DECIMAL                                 NOT NULL,
    balance        BIGINT DEFAULT 0                        NOT NULL
);

CREATE TABLE orders_goods
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    good_name      VARCHAR(255)                            NOT NULL,
    order_id       BIGINT                                  NOT NULL,
    goods_quantity BIGINT                                  NOT NULL,
    good_id        BIGINT,
    sum            BIGINT                                  NOT NULL
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name     VARCHAR(255)                            NOT NULL,
    surname  VARCHAR(255)                            NOT NULL,
    position VARCHAR(255)                            NOT NULL,
    email    VARCHAR(255)                            NOT NULL UNIQUE,
    password VARCHAR(255)                            NOT NULL
);

CREATE TABLE companies
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name      VARCHAR(255)                            NOT NULL,
    address   VARCHAR(255)                            NOT NULL,
    phone     VARCHAR(255),
    email     VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE orders
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    user_id      BIGINT                                  NOT NULL,
    company_id   BIGINT                                  NOT NULL,
    total_amount BIGINT                                  NOT NULL,
    created_at   DATE DEFAULT current_date
);


CREATE TABLE store
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    responsible_person VARCHAR(255)                            NOT NULL,
    good_name          VARCHAR(255)                            NOT NULL,
    purchase_price     DECIMAL                                 NOT NULL,
    arrived_total      BIGINT    DEFAULT 0,
    consumption_total  BIGINT    DEFAULT 0,
    reason             VARCHAR(255)                            NOT NULL,
    user_id            BIGINT                                  NOT NULL,
    created_at         timestamp default current_timestamp     NOT NULL
);

ALTER TABLE goods
    ADD CONSTRAINT fk_goods_on_category FOREIGN KEY (category_id) REFERENCES category (id);
ALTER TABLE orders_goods
    ADD CONSTRAINT fk_orders_goods_on_order_id FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE store
    ADD CONSTRAINT fk_store_on_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_on_companies FOREIGN KEY (company_id) REFERENCES companies (id);