insert into deal(id, nb_product_to_buy, nb_product_discounted, discount) values(1, 2, 1, 50)

insert into product(id, type, name, price, description, remaining_qty, deal_id) values(1, 'LAPTOP', 'DELL 140', 900, 'LAPTOP - DELL 140 16Go Ram', 5, 1), (2, 'MOUSE', 'LOGITECH G35', 70, 'GAMING MOUSE', 7, null),  (3, 'KEYBOARD', 'RAZER X60', 100, 'RAZER Gaming Keyboard', 5, null)

insert into bundle(id, product_id, offered_product_id) values(1,1,2)

insert into BASKET_PRODUCT(id, quantity, product_id) values(1, 5, 1)