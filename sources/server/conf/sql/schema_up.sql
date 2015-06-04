CREATE TABLE account
(
  id SERIAL PRIMARY KEY NOT NULL,
  login VARCHAR(254) NOT NULL,
  password_hash VARCHAR(1000) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  role VARCHAR(254) NOT NULL,
  comment VARCHAR(5000),
  creation_date TIMESTAMP,
  edit_date TIMESTAMP,
  creator_id INT,
  editor_id INT
);
CREATE TABLE car
(
  id SERIAL PRIMARY KEY NOT NULL,
  reg_number VARCHAR(12) NOT NULL,
  make VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  rate NUMERIC(8) NOT NULL,
  mileage NUMERIC(10) NOT NULL,
  service NUMERIC(10),
  comment VARCHAR(5000),
  creation_date TIMESTAMP,
  creator_id INT,
  edit_date TIMESTAMP,
  editor_id INT
);
CREATE TABLE checkpoint
(
  id SERIAL PRIMARY KEY NOT NULL,
  point_date TIMESTAMP NOT NULL,
  creation_date TIMESTAMP,
  creator_id INT,
  edit_date TIMESTAMP,
  editor_id INT,
  comment VARCHAR(5000)
);
CREATE TABLE driver
(
  id SERIAL PRIMARY KEY NOT NULL,
  pass VARCHAR(254) NOT NULL,
  license VARCHAR(254) NOT NULL,
  last_name VARCHAR(254) NOT NULL,
  first_name VARCHAR(254) NOT NULL,
  middle_name VARCHAR(254),
  phone VARCHAR(254) NOT NULL,
  sec_phone VARCHAR(254) NOT NULL,
  comment VARCHAR(254),
  address VARCHAR(254) NOT NULL,
  creation_date TIMESTAMP,
  edit_date TIMESTAMP,
  creator_id INT,
  editor_id INT
);
CREATE TABLE expense
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMP NOT NULL,
  amount NUMERIC(10) NOT NULL,
  subject VARCHAR(255) NOT NULL,
  description VARCHAR(1000),
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP
);
CREATE TABLE fine
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMP NOT NULL,
  amount NUMERIC(10) NOT NULL,
  description VARCHAR(5000),
  rent_id INT NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP
);
CREATE TABLE payment
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMP NOT NULL,
  presence BOOLEAN NOT NULL DEFAULT TRUE,
  amount NUMERIC(8) NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP,
  rent_id INT NOT NULL
);
CREATE TABLE rent
(
  id SERIAL PRIMARY KEY NOT NULL,
  driver_id INT NOT NULL,
  car_id INT NOT NULL,
  deposit NUMERIC(8) NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP
);
CREATE TABLE rent_status
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMP NOT NULL,
  status VARCHAR(255) NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP,
  rent_id INT NOT NULL
);
CREATE TABLE repair
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMP NOT NULL,
  amount NUMERIC(10) NOT NULL,
  description VARCHAR(5000),
  rent_id INT NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMP,
  editor_id INT,
  edit_date TIMESTAMP
);
ALTER TABLE account ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE account ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX idx_login_uq ON account (login);
ALTER TABLE car ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE car ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX unique_reg_number ON car (reg_number);
ALTER TABLE checkpoint ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE checkpoint ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE INDEX point_date_index ON checkpoint (point_date);
ALTER TABLE driver ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE driver ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX idx_license_uq ON driver (license);
CREATE UNIQUE INDEX idx_pass_uq ON driver (pass);
ALTER TABLE expense ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE expense ADD FOREIGN KEY (editor_id) REFERENCES account (id);
CREATE UNIQUE INDEX unique_id ON expense (id);
ALTER TABLE fine ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE fine ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE fine ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE payment ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE payment ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE payment ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE rent ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE rent ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE rent ADD FOREIGN KEY (car_id) REFERENCES car (id);
ALTER TABLE rent ADD FOREIGN KEY (driver_id) REFERENCES driver (id);
ALTER TABLE rent_status ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE rent_status ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE rent_status ADD FOREIGN KEY (rent_id) REFERENCES rent (id) ON DELETE CASCADE;
CREATE INDEX change_date_index ON rent_status (change_time);
CREATE INDEX status_index ON rent_status (status);
ALTER TABLE repair ADD FOREIGN KEY (creator_id) REFERENCES account (id);
ALTER TABLE repair ADD FOREIGN KEY (editor_id) REFERENCES account (id);
ALTER TABLE repair ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
