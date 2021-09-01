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
    login        TEXT NOT NULL UNIQUE ,
    password     TEXT NOT NULL,
    user_name    TEXT NOT NULL,
    surname      TEXT NOT NULL,
    address      TEXT,
    phone_number TEXT,
    is_deleted   BOOLEAN DEFAULT False
);

CREATE TABLE IF NOT EXISTS tickets
(
    id         BIGSERIAL PRIMARY KEY,
    author_id  BIGSERIAL,
    message    TEXT,
    priority   SMALLINT,
    status     TEXT,
    is_deleted BOOLEAN DEFAULT False,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS purchases
(
    id             BIGSERIAL PRIMARY KEY,
    client_id      BIGSERIAL,
    sum_of_payment REAL,
    pur_status         TEXT,
    is_deleted     BOOLEAN DEFAULT False,
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS delivery_orders
(
    id             BIGSERIAL PRIMARY KEY,
    purchase_id    BIGSERIAL,
    date_time      TIMESTAMP,
    location       TEXT,
    addressee      TEXT,
    contact_number TEXT,
    status         TEXT,
    is_deleted     BOOLEAN DEFAULT False,
    FOREIGN KEY (purchase_id) REFERENCES purchases (id)
);



CREATE TABLE IF NOT EXISTS categories
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT,
    is_deleted BOOLEAN DEFAULT False
);

CREATE TABLE IF NOT EXISTS products
(
    id             BIGSERIAL PRIMARY KEY,
    category_id    BIGSERIAL,
    article_number TEXT,
    name           TEXT,
    price          REAL,
    is_deleted     BOOLEAN DEFAULT False,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS purchase_contents
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGSERIAL,
    purchase_id BIGSERIAL,
    amount      INTEGER,
    is_deleted  BOOLEAN DEFAULT False,
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (purchase_id) REFERENCES purchases (id)
);