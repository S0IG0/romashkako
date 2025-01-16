CREATE TABLE sale
(
    id         BIGSERIAL PRIMARY KEY,
    count      BIGINT         NOT NULL CHECK (count > 0),
    name       VARCHAR(255)   NOT NULL,
    price      NUMERIC(38, 2) NOT NULL CHECK (price > 0.00),
    product_id BIGINT         NOT NULL,
    created_at TIMESTAMP(6)   NOT NULL,
    updated_at TIMESTAMP(6)   NOT NULL
);

ALTER TABLE IF EXISTS sale
    ADD CONSTRAINT sale_product_id_fl
        FOREIGN KEY (product_id) REFERENCES product;