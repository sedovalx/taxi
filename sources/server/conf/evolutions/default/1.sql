# --- !Ups

CREATE TABLE operation
(
  id SERIAL PRIMARY KEY NOT NULL,
  rent_id INT NOT NULL,
  amount NUMERIC(10) NOT NULL,
  change_time TIMESTAMPTZ NOT NULL,
  account_type VARCHAR(100) NOT NULL,
  presence BOOLEAN NOT NULL DEFAULT true,
  comment VARCHAR(5000),
  creation_date TIMESTAMPTZ,
  creator_id INT,
  edit_date TIMESTAMPTZ,
  editor_id INT
);
CREATE TABLE car
(
  id SERIAL PRIMARY KEY NOT NULL,
  reg_number VARCHAR(12) NOT NULL,
  make VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  rate NUMERIC(10) NOT NULL,
  mileage NUMERIC(10) NOT NULL,
  service NUMERIC(10),
  comment VARCHAR(5000),
  creation_date TIMESTAMPTZ,
  creator_id INT,
  edit_date TIMESTAMPTZ,
  editor_id INT
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
  creation_date TIMESTAMPTZ,
  edit_date TIMESTAMPTZ,
  creator_id INT,
  editor_id INT
);
CREATE TABLE rent
(
  id SERIAL PRIMARY KEY NOT NULL,
  driver_id INT NOT NULL,
  car_id INT NOT NULL,
  deposit NUMERIC(10) NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMPTZ,
  editor_id INT,
  edit_date TIMESTAMPTZ
);
CREATE TABLE rent_status
(
  id SERIAL PRIMARY KEY NOT NULL,
  change_time TIMESTAMPTZ NOT NULL,
  status VARCHAR(255) NOT NULL,
  comment VARCHAR(5000),
  creator_id INT,
  creation_date TIMESTAMPTZ,
  editor_id INT,
  edit_date TIMESTAMPTZ,
  rent_id INT NOT NULL
);
CREATE TABLE system_user
(
  id SERIAL PRIMARY KEY NOT NULL,
  login VARCHAR(254) NOT NULL,
  password_hash VARCHAR(1000) NOT NULL,
  last_name VARCHAR(254),
  first_name VARCHAR(254),
  middle_name VARCHAR(254),
  role VARCHAR(254) NOT NULL,
  comment VARCHAR(5000),
  creation_date TIMESTAMPTZ,
  edit_date TIMESTAMPTZ,
  creator_id INT,
  editor_id INT
);
ALTER TABLE operation ADD FOREIGN KEY (rent_id) REFERENCES rent (id);
ALTER TABLE operation ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE operation ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
ALTER TABLE car ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE car ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
CREATE UNIQUE INDEX unique_reg_number ON car (reg_number);
ALTER TABLE driver ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE driver ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
CREATE UNIQUE INDEX idx_license_uq ON driver (license);
CREATE UNIQUE INDEX idx_pass_uq ON driver (pass);
ALTER TABLE rent ADD FOREIGN KEY (car_id) REFERENCES car (id);
ALTER TABLE rent ADD FOREIGN KEY (driver_id) REFERENCES driver (id);
ALTER TABLE rent ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE rent ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
ALTER TABLE rent_status ADD FOREIGN KEY (rent_id) REFERENCES rent (id) ON DELETE CASCADE;
ALTER TABLE rent_status ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE rent_status ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
CREATE INDEX change_date_index ON rent_status (change_time);
CREATE INDEX status_index ON rent_status (status);
ALTER TABLE system_user ADD FOREIGN KEY (creator_id) REFERENCES system_user (id);
ALTER TABLE system_user ADD FOREIGN KEY (editor_id) REFERENCES system_user (id);
CREATE UNIQUE INDEX idx_login_uq ON system_user (login);

# --- !Downs

DROP TABLE operation;
DROP TABLE rent_status;
DROP TABLE rent;
DROP TABLE car;
DROP TABLE driver;
DROP TABLE system_user;

