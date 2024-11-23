CREATE TABLE categories
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    category VARCHAR(255)
);

CREATE TABLE goods
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(255),
    category_id BIGINT,
    price       DECIMAL,
    quantity    BIGINT
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name     VARCHAR(255),
    surname  VARCHAR(255),
    position VARCHAR(255),
    email    VARCHAR(255) UNIQUE ,
    password VARCHAR(255),
    role     VARCHAR(255)
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
    company_id BIGINT
);

ALTER TABLE goods
    ADD CONSTRAINT fk_goods_on_category FOREIGN KEY (category_id) REFERENCES categories (id);