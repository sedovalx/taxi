# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "account" ("id" SERIAL NOT NULL PRIMARY KEY,"login" VARCHAR(254) NOT NULL,"password_hash" VARCHAR(1000) NOT NULL,"last_name" VARCHAR(254) DEFAULT null,"first_name" VARCHAR(254) DEFAULT null,"middle_name" VARCHAR(254) DEFAULT null,"role" VARCHAR(254) NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"creator_id" INTEGER DEFAULT null,"editor_id" INTEGER DEFAULT null);
create unique index "idx_login_uq" on "account" ("login");
create table "car" ("id" SERIAL NOT NULL PRIMARY KEY,"reg_number" VARCHAR(12) NOT NULL,"make" VARCHAR(255) NOT NULL,"model" VARCHAR(255) NOT NULL,"rate" DECIMAL(21,2) NOT NULL,"mileage" DECIMAL(21,2) NOT NULL,"service" DECIMAL(21,2) DEFAULT null,"comment" VARCHAR(5000) DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"creator_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null);
create unique index "unique_reg_number" on "car" ("reg_number");
create table "checkpoint" ("id" SERIAL NOT NULL PRIMARY KEY,"point_date" TIMESTAMP NOT NULL,"creation_date" TIMESTAMP DEFAULT null,"creator_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"comment" VARCHAR(5000) DEFAULT null);
create index "point_date_index" on "checkpoint" ("point_date");
create table "driver" ("id" SERIAL NOT NULL PRIMARY KEY,"pass" VARCHAR(254) NOT NULL,"license" VARCHAR(254) NOT NULL,"last_name" VARCHAR(254) NOT NULL,"first_name" VARCHAR(254) NOT NULL,"middle_name" VARCHAR(254) DEFAULT null,"phone" VARCHAR(254) NOT NULL,"sec_phone" VARCHAR(254) NOT NULL,"comment" VARCHAR(254) DEFAULT null,"address" VARCHAR(254) NOT NULL,"creation_date" TIMESTAMP DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"creator_id" INTEGER DEFAULT null,"editor_id" INTEGER DEFAULT null);
create unique index "idx_license_uq" on "driver" ("license");
create unique index "idx_pass_uq" on "driver" ("pass");
create table "expense" ("id" SERIAL NOT NULL PRIMARY KEY,"change_time" TIMESTAMP NOT NULL,"amount" DECIMAL(21,2) NOT NULL,"subject" VARCHAR(255) NOT NULL,"description" VARCHAR(1000) DEFAULT null,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null);
create table "fine" ("id" SERIAL NOT NULL PRIMARY KEY,"change_time" TIMESTAMP NOT NULL,"amount" DECIMAL(21,2) NOT NULL,"description" VARCHAR(5000) DEFAULT null,"rent_id" INTEGER NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null);
create table "payment" ("id" SERIAL NOT NULL PRIMARY KEY,"change_time" TIMESTAMP NOT NULL,"amount" DECIMAL(21,2) NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"rent_id" INTEGER NOT NULL);
create table "rent" ("id" SERIAL NOT NULL PRIMARY KEY,"driver_id" INTEGER NOT NULL,"car_id" INTEGER NOT NULL,"deposit" DECIMAL(21,2) NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null);
create table "rent_status" ("id" SERIAL NOT NULL PRIMARY KEY,"change_time" TIMESTAMP NOT NULL,"status" VARCHAR(255) NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null,"rent_id" INTEGER NOT NULL);
create index "change_date_index" on "rent_status" ("change_time");
create index "status_index" on "rent_status" ("status");
create table "repair" ("id" SERIAL NOT NULL PRIMARY KEY,"change_time" TIMESTAMP NOT NULL,"amount" DECIMAL(21,2) NOT NULL,"description" VARCHAR(5000) DEFAULT null,"rent_id" INTEGER NOT NULL,"comment" VARCHAR(5000) DEFAULT null,"creator_id" INTEGER DEFAULT null,"creation_date" TIMESTAMP DEFAULT null,"editor_id" INTEGER DEFAULT null,"edit_date" TIMESTAMP DEFAULT null);
alter table "account" add constraint "account_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "account" add constraint "account_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "car" add constraint "car_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "car" add constraint "car_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "checkpoint" add constraint "checkpoint_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "checkpoint" add constraint "checkpoint_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "driver" add constraint "driver_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "driver" add constraint "driver_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "expense" add constraint "expense_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "expense" add constraint "expense_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "fine" add constraint "fine_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "fine" add constraint "fine_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "fine" add constraint "fine_rent_id_fkey" foreign key("rent_id") references "rent"("id") on update NO ACTION on delete NO ACTION;
alter table "payment" add constraint "payment_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "payment" add constraint "payment_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "payment" add constraint "payment_rent_id_fkey" foreign key("rent_id") references "rent"("id") on update NO ACTION on delete NO ACTION;
alter table "rent" add constraint "rent_car_id_fkey" foreign key("car_id") references "car"("id") on update NO ACTION on delete NO ACTION;
alter table "rent" add constraint "rent_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "rent" add constraint "rent_driver_id_fkey" foreign key("driver_id") references "driver"("id") on update NO ACTION on delete NO ACTION;
alter table "rent" add constraint "rent_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "rent_status" add constraint "rent_status_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "rent_status" add constraint "rent_status_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "rent_status" add constraint "rent_status_rent_id_fkey" foreign key("rent_id") references "rent"("id") on update NO ACTION on delete CASCADE;
alter table "repair" add constraint "repair_creator_id_fkey" foreign key("creator_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "repair" add constraint "repair_editor_id_fkey" foreign key("editor_id") references "account"("id") on update NO ACTION on delete NO ACTION;
alter table "repair" add constraint "repair_rent_id_fkey" foreign key("rent_id") references "rent"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "repair" drop constraint "repair_creator_id_fkey";
alter table "repair" drop constraint "repair_editor_id_fkey";
alter table "repair" drop constraint "repair_rent_id_fkey";
alter table "rent_status" drop constraint "rent_status_creator_id_fkey";
alter table "rent_status" drop constraint "rent_status_editor_id_fkey";
alter table "rent_status" drop constraint "rent_status_rent_id_fkey";
alter table "rent" drop constraint "rent_car_id_fkey";
alter table "rent" drop constraint "rent_creator_id_fkey";
alter table "rent" drop constraint "rent_driver_id_fkey";
alter table "rent" drop constraint "rent_editor_id_fkey";
alter table "payment" drop constraint "payment_creator_id_fkey";
alter table "payment" drop constraint "payment_editor_id_fkey";
alter table "payment" drop constraint "payment_rent_id_fkey";
alter table "fine" drop constraint "fine_creator_id_fkey";
alter table "fine" drop constraint "fine_editor_id_fkey";
alter table "fine" drop constraint "fine_rent_id_fkey";
alter table "expense" drop constraint "expense_creator_id_fkey";
alter table "expense" drop constraint "expense_editor_id_fkey";
alter table "driver" drop constraint "driver_creator_id_fkey";
alter table "driver" drop constraint "driver_editor_id_fkey";
alter table "checkpoint" drop constraint "checkpoint_creator_id_fkey";
alter table "checkpoint" drop constraint "checkpoint_editor_id_fkey";
alter table "car" drop constraint "car_creator_id_fkey";
alter table "car" drop constraint "car_editor_id_fkey";
alter table "account" drop constraint "account_creator_id_fkey";
alter table "account" drop constraint "account_editor_id_fkey";
drop table "repair";
drop table "rent_status";
drop table "rent";
drop table "payment";
drop table "fine";
drop table "expense";
drop table "driver";
drop table "checkpoint";
drop table "car";
drop table "account";

