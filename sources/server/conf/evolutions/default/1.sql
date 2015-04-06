# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "driver" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"pass" VARCHAR(254) NOT NULL,"driverCard" VARCHAR(254) NOT NULL,"last_name" VARCHAR(254),"first_name" VARCHAR(254),"middle_name" VARCHAR(254),"phone" VARCHAR(254) NOT NULL,"secPhone" VARCHAR(254) NOT NULL,"comment" VARCHAR(254),"address" VARCHAR(254) NOT NULL,"creation_date" DATE,"edit_date" DATE,"creator_id" BIGINT,"editor_id" BIGINT);
create unique index "idx_license_uq" on "driver" ("driverCard");
create unique index "idx_pass_uq" on "driver" ("pass");
create table "user" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"login" VARCHAR(254) NOT NULL,"password_hash" VARCHAR(1000) NOT NULL,"last_name" VARCHAR(254),"first_name" VARCHAR(254),"middle_name" VARCHAR(254),"role" VARCHAR(254) NOT NULL,"creation_date" DATE,"edit_date" DATE,"creator_id" BIGINT,"editor_id" BIGINT);
create unique index "idx_login_uq" on "user" ("login");
alter table "driver" add constraint "driver_creator_id_fkey" foreign key("creator_id") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "driver" add constraint "driver_editor_id_fkey" foreign key("editor_id") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "user" add constraint "user_creator_id_fkey" foreign key("creator_id") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "user" add constraint "user_editor_id_fkey" foreign key("editor_id") references "user"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "user" drop constraint "user_creator_id_fkey";
alter table "user" drop constraint "user_editor_id_fkey";
alter table "driver" drop constraint "driver_creator_id_fkey";
alter table "driver" drop constraint "driver_editor_id_fkey";
drop table "user";
drop table "driver";

