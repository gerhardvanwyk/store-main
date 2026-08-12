-- Reset sequences for tables with BIGSERIAL columns
SELECT setval('customer_id_seq', (SELECT MAX(id) FROM customer));
SELECT setval('order_id_seq', (SELECT MAX(id) FROM "order"));
SELECT setval('product_id_seq', (SELECT MAX(id) FROM product));
