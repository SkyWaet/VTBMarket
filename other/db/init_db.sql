CREATE DATABASE "VTBMarket"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    name         TEXT NOT NULL,
    surname      TEXT NOT NULL,
    address      TEXT,
    phone_number TEXT
);

CREATE TABLE IF NOT EXISTS tickets
(
    id        BIGSERIAL PRIMARY KEY,
    author_id BIGSERIAL,
    message   TEXT,
    priority  SMALLINT,
    status    TEXT,
    FOREIGN KEY (author_id) REFERENCES users (id)

);

CREATE TABLE IF NOT EXISTS delivery_orders
(
    id             BIGSERIAL PRIMARY KEY,
    date_time      TIMESTAMP,
    location       TEXT,
    addressee      TEXT,
    contact_number TEXT,
    status         TEXT
);

CREATE TABLE IF NOT EXISTS purchases
(
    id             BIGSERIAL PRIMARY KEY,
    delivery_id    BIGSERIAL,
    client_id      BIGSERIAL,
    sum_of_payment REAL,
    status         TEXT,
    FOREIGN KEY (delivery_id) REFERENCES delivery_orders (id),
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS products
(
    id             BIGSERIAL PRIMARY KEY,
    category_id    BIGSERIAL,
    article_number TEXT,
    name           TEXT,
    price          REAL,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS purchase_contents
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGSERIAL,
    purchase_id BIGSERIAL,
    amount      INTEGER,
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (purchase_id) REFERENCES purchases (id)
);