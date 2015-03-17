# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "user" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"login" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"last_name" VARCHAR(254),"first_name" VARCHAR(254),"middle_name" VARCHAR(254),"role" VARCHAR(254) NOT NULL,"creation_date" DATE NOT NULL,"edit_date" DATE,"creator_id" BIGINT,"editor_id" BIGINT);
create unique index "idx_login_uq" on "user" ("login");
alter table "user" add constraint "fk_user_creator" foreign key("creator_id") references "user"("id") on update NO ACTION on delete NO ACTION;
alter table "user" add constraint "fk_user_editor" foreign key("editor_id") references "user"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "user" drop constraint "fk_user_creator";
alter table "user" drop constraint "fk_user_editor";
drop table "user";

