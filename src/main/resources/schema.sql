DROP TABLE IF EXISTS BUNDLE;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS DEAL;

CREATE TABLE DEAL (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    nb_product_to_buy INT NOT NULL,
    nb_product_discounted INT NOT NULL,
    discount INT NOT NULL
);

CREATE TABLE PRODUCT (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  type VARCHAR(250) NOT NULL,
  name VARCHAR(250) NOT NULL,
  price INT NOT NULL,
  description TEXT DEFAULT NULL,
  remaining_qty INT NOT NULL,
  deal_id INT DEFAULT NULL,
  foreign key (deal_id) references DEAL(id)
);

CREATE TABLE BUNDLE (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    id_product INT NOT NULL,
    foreign key (id_product) references PRODUCT(id),
    id_offered_product INT NOT NULL,
    foreign key (id_offered_product) references PRODUCT(id)
);
