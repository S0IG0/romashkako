create table product
(
    id           BIGSERIAL PRIMARY KEY,
    price        NUMERIC(38, 2) NOT NULL                                                      DEFAULT 0.00,
    name         VARCHAR(255)   NOT NULL,
    description  VARCHAR(4096),
    availability VARCHAR(255)   NOT NULL CHECK (availability IN ('IN_STOCK', 'OUT_OF_STOCK')) DEFAULT 'OUT_OF_STOCK'
);
