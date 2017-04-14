create table "owners" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"name" VARCHAR NOT NULL,"email" VARCHAR NOT NULL,"street" VARCHAR NOT NULL,"city" VARCHAR NOT NULL,"state" VARCHAR NOT NULL,"zip" INTEGER NOT NULL)
create table "pools" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"owner_id" INTEGER NOT NULL,"gallons" DOUBLE NOT NULL)
create table "cleanings" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"deck" BOOLEAN NOT NULL,"brush" BOOLEAN NOT NULL,"net" BOOLEAN NOT NULL,"basket" BOOLEAN NOT NULL,"filter" BOOLEAN NOT NULL)
create table "measurements" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"ci" INTEGER NOT NULL,"ph" INTEGER NOT NULL,"alky" INTEGER NOT NULL,"temp" INTEGER NOT NULL)
create table "chemicals" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"name" VARCHAR NOT NULL,"unit" VARCHAR NOT NULL)
create table "additives" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"chemical_id" INTEGER NOT NULL,"on" DATE NOT NULL,"amount" DOUBLE NOT NULL)
create table "repairs" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"cost" DOUBLE NOT NULL,"description" VARCHAR NOT NULL)
alter table "pools" add constraint "owner_fk" foreign key("owner_id") references "owners"("id") on update NO ACTION on delete NO ACTION
alter table "cleanings" add constraint "pool_cleaning_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "measurements" add constraint "pool_measurement_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "additives" add constraint "pool_additive_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "repairs" add constraint "pool_repair_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION