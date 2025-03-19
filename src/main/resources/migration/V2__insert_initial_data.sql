INSERT INTO brands (id, name)
VALUES (1, 'ZARA');

INSERT INTO products (id, name, description)
VALUES (35455, 'JEANS MARINE',
        'Jeans marine fit con cinturilla interior ajustable y cierre botón frontal. Bolsillos tipo plastrón en espalda.');

INSERT INTO price_list (id, description)
VALUES
    (1, 'Listado de Precios del 2020 - 1'),
    (2, 'Listado de Precios del 2020 - 2'),
    (3, 'Listado de Precios del 2020 - 3'),
    (4, 'Listado de Precios del 2020 - 4');
;

INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr)
VALUES (1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),
       (1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),
       (1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),
       (1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');
