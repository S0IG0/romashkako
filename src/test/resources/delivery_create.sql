INSERT INTO product (price, name, description, availability, count, deleted)
VALUES (0.00, 'Product 0', 'Description for Product 0', 'IN_STOCK', 10, false),
       (1.00, 'Product 1', 'Description for Product 1', 'IN_STOCK', 10, false),
       (2.00, 'Product 2', 'Description for Product 2', 'IN_STOCK', 10, false),
       (3.00, 'Product 3', 'Description for Product 3', 'IN_STOCK', 10, false),
       (4.00, 'Product 4', 'Description for Product 4', 'IN_STOCK', 10, false),
       (5.00, 'Product 5', 'Description for Product 5', 'IN_STOCK', 10, false),
       (6.00, 'Product 6', 'Description for Product 6', 'IN_STOCK', 10, false),
       (7.00, 'Product 7', 'Description for Product 7', 'IN_STOCK', 10, false),
       (8.00, 'Product 8', 'Description for Product 8', 'IN_STOCK', 10, false),
       (9.00, 'Product 9', 'Description for Product 9', 'IN_STOCK', 10, false);

INSERT INTO delivery (name, count, product_id, created_at, updated_at)
VALUES ('Delivery 0', 10, 1, NOW(), NOW()),
       ('Delivery 1', 10, 2, NOW(), NOW()),
       ('Delivery 2', 10, 3, NOW(), NOW()),
       ('Delivery 3', 10, 4, NOW(), NOW()),
       ('Delivery 4', 10, 5, NOW(), NOW()),
       ('Delivery 5', 10, 6, NOW(), NOW()),
       ('Delivery 6', 10, 7, NOW(), NOW()),
       ('Delivery 7', 10, 8, NOW(), NOW()),
       ('Delivery 8', 10, 9, NOW(), NOW()),
       ('Delivery 9', 10, 10, NOW(), NOW());
