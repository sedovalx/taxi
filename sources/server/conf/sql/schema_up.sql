CREATE TABLE car
(
  id SERIAL PRIMARY KEY NOT NULL,
  reg_number VARCHAR(12) NOT NULL,
  make VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  mileage NUMERIC(10,2) NOT NULL,
  service NUMERIC(10,2),
  comment VARCHAR,
  creation_date DATE,
  creator_id INTEGER,
  edit_date DATE,
  editor_id INTEGER,
  class_id INTEGER NOT NULL
);
CREATE TABLE car_class
(
  id SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL,
  rate NUMERIC(8,2) NOT NULL,
  creation_date DATE,
  creator_id INTEGER,
  edit_date DATE,
  editor_id INTEGER,
  comment VARCHAR
);
CREATE TABLE checkpoint
(
  id SERIAL PRIMARY KEY NOT NULL,
  point_date DATE NOT NULL,
  days INT NOT NULL,
  creation_date DATE,
  creator_id INTEGER,
  edit_date DATE,
  editor_id INTEGER,
  comment VARCHAR
);
CREATE TABLE driver
(
  id SERIAL PRIMARY KEY NOT NULL,
  pass VARCHAR(254) NOT NULL,
  license VARCHAR(254) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  phone VARCHAR(254) NOT NULL,
  sec_phone VARCHAR(254) NOT NULL,
  comment VARCHAR(254),
  address VARCHAR(254) NOT NULL,
  creation_date DATE,
  edit_date DATE,
  creator_id INTEGER,
  editor_id INTEGER
);
CREATE TABLE fine
(
  id SERIAL PRIMARY KEY NOT NULL,
  fine_date DATE NOT NULL,
  cost NUMERIC(10,2) NOT NULL,
  description VARCHAR,
  rent_id INTEGER NOT NULL,
  comment VARCHAR,
  creator_id INTEGER,
  creation_date DATE,
  editor_id INTEGER,
  edit_date DATE
);
CREATE TABLE payment
(
  id SERIAL PRIMARY KEY NOT NULL,
  pay_date DATE NOT NULL,
  amount NUMERIC(8,2) NOT NULL,
  target VARCHAR(255) NOT NULL,
  comment VARCHAR,
  creator_id INTEGER,
  creation_date DATE,
  editor_id INTEGER,
  edit_date DATE,
  rent_id INTEGER NOT NULL
);
CREATE TABLE rent
(
  id SERIAL PRIMARY KEY NOT NULL,
  driver_id INTEGER NOT NULL,
  car_id INTEGER NOT NULL,
  deposit NUMERIC(8,2) NOT NULL,
  comment VARCHAR,
  creator_id INTEGER,
  creation_date DATE,
  editor_id INTEGER,
  edit_date DATE
);
CREATE TABLE rent_status
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_date DATE NOT NULL,
  status VARCHAR(255) NOT NULL,
  comment VARCHAR,
  creator_id INTEGER,
  creation_date DATE,
  editor_id INTEGER,
  edit_date DATE
);
CREATE TABLE repair
(
  id SERIAL PRIMARY KEY NOT NULL,
  repair_date DATE NOT NULL,
  cost NUMERIC(10,2) NOT NULL,
  description VARCHAR,
  rent_id INTEGER NOT NULL,
  comment VARCHAR,
  creator_id INTEGER,
  creation_date DATE,
  editor_id INTEGER,
  edit_date DATE
);
CREATE TABLE account
(
  id SERIAL PRIMARY KEY NOT NULL,
  login VARCHAR(254) NOT NULL,
  password_hash VARCHAR(1000) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  role VARCHAR(254) NOT NULL,
  comment VARCHAR,
  creation_date DATE,
  edit_date DATE,
  creator_id INTEGER,
  editor_id INTEGER
);

ALTER TABLE car ADD FOREIGN KEY (class_id) REFERENCES car_class (id);
ALTER TABLE car ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE car ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX unique_reg_number ON car (reg_number);
ALTER TABLE car_class ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE car_class ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX unique_name ON car_class (name);
ALTER TABLE checkpoint ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE checkpoint ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE INDEX point_date_index ON checkpoint (point_date);
ALTER TABLE driver ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE driver ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX idx_license_uq ON driver (license);
CREATE UNIQUE INDEX idx_pass_uq ON driver (pass);
ALTER TABLE fine ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE fine ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE fine ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE payment ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE payment ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE payment ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE rent ADD FOREIGN KEY (car_id) REFERENCES car (id);
ALTER TABLE rent ADD FOREIGN KEY (driver_id) REFERENCES driver (id);
ALTER TABLE rent ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE rent ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE rent_status ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE rent_status ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE INDEX change_date_index ON rent_status (change_date);
CREATE INDEX status_index ON rent_status (status);
ALTER TABLE repair ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE repair ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE repair ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE account ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE account ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX idx_login_uq ON account (login);
