CREATE TABLE delivery
(
    id         BIGSERIAL PRIMARY KEY,
    count      BIGINT       NOT NULL CHECK (count > 0),
    name       VARCHAR(255) NOT NULL,
    product_id BIGINT       NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

ALTER TABLE IF EXISTS delivery
    ADD CONSTRAINT delivery_product_id_fl
        FOREIGN KEY (product_id) REFERENCES product;