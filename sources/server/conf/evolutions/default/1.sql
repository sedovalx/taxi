# --- !Ups

CREATE TABLE operation
(
  id SERIAL PRIMARY KEY NOT NULL,
  rent_id INT NOT NULL,
  amount NUMERIC(10,2) NOT NULL,
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
  rate NUMERIC(10,2) NOT NULL,
  mileage NUMERIC(10,2) NOT NULL,
  service NUMERIC(10,2),
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
  deposit NUMERIC(10,2) NOT NULL,
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

CREATE OR REPLACE VIEW rent_last_status AS
  SELECT
    *
  FROM
    (
      SELECT
        rs.*,
        rank() OVER (PARTITION BY rs.rent_id ORDER BY rs.change_time DESC ) AS rk
      FROM rent_status rs
    ) t
  WHERE t.rk = 1;

CREATE OR REPLACE VIEW payments AS
  SELECT *
  FROM operation o
  WHERE o.account_type = 'Rent';

CREATE OR REPLACE VIEW fines AS
  SELECT *
  FROM operation o
  WHERE o.account_type = 'Fine';

CREATE OR REPLACE VIEW repairs AS
  SELECT *
  FROM operation o
  WHERE o.account_type = 'Repair';

CREATE OR REPLACE VIEW car_last_rent AS
  SELECT
    c.*,
    r.id AS "rent_id"
  FROM car c
    LEFT JOIN (
      SELECT
        *
      FROM
        (
          SELECT
            r.*,
            rank() OVER (PARTITION BY r.car_id ORDER BY r.creation_date) AS rk
          FROM rent r
        ) t
      WHERE t.rk = 1
    ) r on c.id = r.car_id;


CREATE OR REPLACE FUNCTION func_payments(control_date TIMESTAMPTZ)
  RETURNS TABLE(rent_id INT, amount NUMERIC(10,2))
AS $$
BEGIN
  RETURN QUERY
  SELECT
    r.id,
    sum(coalesce(p.amount, 0)) as "payments"
  FROM rent r
  LEFT JOIN payments p ON p.rent_id = r.id AND p.creation_date < control_date
  GROUP BY r.id;;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION func_fines(control_date TIMESTAMPTZ)
  RETURNS TABLE(rent_id INT, amount NUMERIC(10,2))
AS $$
BEGIN
  RETURN QUERY
  SELECT
    r.id,
    sum(coalesce(p.amount, 0))
  FROM rent r
  LEFT JOIN fines p ON p.rent_id = r.id AND p.creation_date < control_date
  GROUP BY r.id;;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION func_repairs(control_date TIMESTAMPTZ)
  RETURNS TABLE(rent_id INT, amount NUMERIC(10,2))
AS $$
BEGIN
  RETURN QUERY
  SELECT
    r.id,
    sum(coalesce(p.amount, 0))
  FROM rent r
  LEFT JOIN repairs p ON p.rent_id = r.id AND p.creation_date < control_date
  GROUP BY r.id;;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION func_rent_balances(control_date TIMESTAMPTZ)
  RETURNS TABLE(
  rent_id INT,
  driver_id INT,
  car_id INT,
  deposit NUMERIC(10,2),
  creation_date TIMESTAMPTZ,
  status VARCHAR(255),
  payments NUMERIC(10,2),
  fines NUMERIC(10,2),
  repairs NUMERIC(10,2),
  total NUMERIC(10,2))
AS $$
BEGIN
  RETURN QUERY
  SELECT
    r.id,
    r.driver_id,
    r.car_id,
    r.deposit,
    r.creation_date,
    rls.status,
    coalesce(fp.amount, 0),
    coalesce(ff.amount, 0),
    coalesce(fr.amount, 0),
    coalesce(fp.amount, 0) - coalesce(ff.amount, 0) - coalesce(fr.amount, 0)
  FROM rent r
  JOIN func_payments(control_date) fp ON fp.rent_id = r.id
  JOIN func_fines(control_date) ff ON ff.rent_id = r.id
  JOIN func_repairs(control_date) fr ON fr.rent_id = r.id
  LEFT JOIN rent_last_status rls ON rls.rent_id = r.id;;

END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION func_cashier(control_date TIMESTAMPTZ)
  RETURNS TABLE(
  car_id INT,
  rent_id INT,
  rent_creation_date TIMESTAMPTZ,
  driver_id INT,
  car VARCHAR(500),
  driver VARCHAR(500),
  presence BOOLEAN,
  payments NUMERIC(10,2),
  fines NUMERIC(10,2),
  repairs NUMERIC(10,2),
  total NUMERIC(10,2),
  mileage NUMERIC(10,2),
  service NUMERIC(10,2),
  status VARCHAR(255))
AS $$
BEGIN
  RETURN QUERY
  SELECT
    c.id as "car_id",
    r.rent_id,
    r.creation_date,
    r.driver_id,
    (c.reg_number || ' ' || c.make || ' (' || c.rate || ')')::VARCHAR(500) as "car",
    (d.last_name || ' ' || d.first_name || coalesce(' ' || d.middle_name, ''))::VARCHAR(500) as "driver",
    coalesce(p.presence, false) as "presence",
    r.payments,
    r.fines,
    r.repairs,
    r.total,
    c.mileage,
    c.service,
    r.status
  FROM car_last_rent c
  LEFT JOIN func_rent_balances(control_date) r ON c.rent_id = r.rent_id AND r.status <> 'Closed'
  LEFT JOIN driver d on r.driver_id = d.id
  LEFT JOIN payments p on p.rent_id = r.rent_id AND p.change_time::DATE = control_date::DATE;;
END
$$ LANGUAGE plpgsql;

# --- !Downs

DROP FUNCTION IF EXISTS func_cashier(TIMESTAMPTZ);
DROP FUNCTION IF EXISTS func_rent_balances(TIMESTAMPTZ);
DROP FUNCTION IF EXISTS func_payments(TIMESTAMPTZ);
DROP FUNCTION IF EXISTS func_fines(TIMESTAMPTZ);
DROP FUNCTION IF EXISTS func_repairs(TIMESTAMPTZ);

DROP VIEW IF EXISTS car_last_rent;
DROP VIEW IF EXISTS rent_last_status;
DROP VIEW IF EXISTS payments;
DROP VIEW IF EXISTS repairs;
DROP VIEW IF EXISTS fines;

DROP TABLE IF EXISTS operation;
DROP TABLE IF EXISTS rent_status;
DROP TABLE IF EXISTS rent;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS driver;
DROP TABLE IF EXISTS system_user;

