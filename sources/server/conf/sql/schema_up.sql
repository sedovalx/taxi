CREATE TABLE car
(
  id SERIAL PRIMARY KEY NOT NULL,
  reg_number VARCHAR(12) NOT NULL,
  make VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  mileage NUMERIC(8) NOT NULL,
  service NUMERIC(8),
  comment VARCHAR,
  creation_date DATE,
  creator_id BIGINT,
  edit_date DATE,
  editor_id BIGINT,
  class_id BIGINT NOT NULL
);
CREATE TABLE car_class
(
  id SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL,
  rate NUMERIC(8) NOT NULL,
  creation_date DATE,
  creator_id BIGINT,
  edit_date DATE,
  editor_id BIGINT,
  comment VARCHAR
);
CREATE TABLE driver
(
  id SERIAL PRIMARY KEY NOT NULL,
  pass VARCHAR(254) NOT NULL,
  "driverCard" VARCHAR(254) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  phone VARCHAR(254) NOT NULL,
  "secPhone" VARCHAR(254) NOT NULL,
  comment VARCHAR(254),
  address VARCHAR(254) NOT NULL,
  creation_date DATE,
  edit_date DATE,
  creator_id BIGINT,
  editor_id BIGINT
);
CREATE TABLE fine
(
  id SERIAL PRIMARY KEY NOT NULL,
  fine_date DATE NOT NULL,
  cost NUMERIC(10) NOT NULL,
  description VARCHAR,
  rent_id BIGINT NOT NULL,
  comment VARCHAR,
  creator_id BIGINT,
  creation_date DATE,
  editor_id BIGINT,
  edit_date DATE
);
CREATE TABLE payment
(
  id SERIAL PRIMARY KEY NOT NULL,
  pay_date DATE NOT NULL,
  amount NUMERIC(8) NOT NULL,
  target VARCHAR(255) NOT NULL,
  comment VARCHAR,
  creator_id BIGINT,
  creation_date DATE,
  editor_id BIGINT,
  edit_date DATE,
  rent_id BIGINT NOT NULL
);
CREATE TABLE rent
(
  id SERIAL PRIMARY KEY NOT NULL,
  driver_id BIGINT NOT NULL,
  car_id BIGINT NOT NULL,
  deposit NUMERIC(8) NOT NULL,
  comment VARCHAR,
  creator_id BIGINT,
  creation_date DATE,
  editor_id BIGINT,
  edit_date DATE
);
CREATE TABLE rent_status
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_date DATE NOT NULL,
  status VARCHAR(255) NOT NULL,
  comment VARCHAR,
  creator_id BIGINT,
  creation_date DATE,
  editor_id BIGINT,
  edit_date DATE
);
CREATE TABLE repair
(
  id SERIAL PRIMARY KEY NOT NULL,
  repair_date DATE NOT NULL,
  cost NUMERIC(10) NOT NULL,
  description VARCHAR,
  rent_id BIGINT NOT NULL,
  comment VARCHAR,
  creator_id BIGINT,
  creation_date DATE,
  editor_id BIGINT,
  edit_date DATE
);
CREATE TABLE "user"
(
  id SERIAL PRIMARY KEY NOT NULL,
  login VARCHAR(254) NOT NULL,
  password_hash VARCHAR(1000) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  role VARCHAR(254) NOT NULL,
  creation_date DATE,
  edit_date DATE,
  creator_id BIGINT,
  editor_id BIGINT
);
ALTER TABLE car ADD FOREIGN KEY (class_id) REFERENCES car_class (id);
ALTER TABLE car ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE car ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
CREATE UNIQUE INDEX unique_reg_number ON car (reg_number);
ALTER TABLE car_class ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE car_class ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
CREATE UNIQUE INDEX unique_name ON car_class (name);
ALTER TABLE driver ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE driver ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
CREATE UNIQUE INDEX idx_license_uq ON driver ("driverCard");
CREATE UNIQUE INDEX idx_pass_uq ON driver (pass);
ALTER TABLE fine ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE fine ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE fine ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
ALTER TABLE payment ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE payment ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE payment ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
ALTER TABLE rent ADD FOREIGN KEY (car_id) REFERENCES car (id);
ALTER TABLE rent ADD FOREIGN KEY (driver_id) REFERENCES driver (id);
ALTER TABLE rent ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE rent ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
ALTER TABLE rent_status ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE rent_status ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
ALTER TABLE repair ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE repair ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE repair ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
ALTER TABLE "user" ADD FOREIGN KEY (creator_id) REFERENCES "user" (id);
ALTER TABLE "user" ADD FOREIGN KEY (editor_id) REFERENCES "user" (id);
CREATE UNIQUE INDEX idx_login_uq ON "user" (login);