insert into product(id, type, name, price, description, remaining_qty) values(1, 'LAPTOP', 'DELL 150', 700, 'LAPTOP - DELL 150 16Go Ram', 5), (2, 'MOUSE', 'LOGITECH G35', 70, 'GAMING MOUSE', 7)

insert into deal(id, nb_product_to_buy, nb_product_discounted, discount, id_product) values(1, 2, 1, 50, 1)

insert into bundle(id, id_product, id_offered_product) values(1, 1, 2)