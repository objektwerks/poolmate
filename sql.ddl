create table "pools" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"built" DATE NOT NULL,"gallons" INTEGER NOT NULL,"street" VARCHAR NOT NULL,"city" VARCHAR NOT NULL,"state" VARCHAR NOT NULL,"zip" INTEGER NOT NULL)
create table "owners" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"since" DATE NOT NULL,"first" VARCHAR NOT NULL,"last" VARCHAR NOT NULL,"email" VARCHAR NOT NULL)
create table "surfaces" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"installed" DATE NOT NULL,"kind" VARCHAR NOT NULL)
create table "pumps" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"installed" DATE NOT NULL,"model" VARCHAR NOT NULL)
create table "timers" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"installed" DATE NOT NULL,"model" VARCHAR NOT NULL)
create table "heaters" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"installed" DATE NOT NULL,"model" VARCHAR NOT NULL)
create table "lifecycles" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"created" DATE NOT NULL,"active" BOOLEAN NOT NULL,"pump_on" VARCHAR NOT NULL,"pump_off" VARCHAR NOT NULL)
create table "cleanings" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"deck" BOOLEAN NOT NULL,"brush" BOOLEAN NOT NULL,"net" BOOLEAN NOT NULL,"vacuum" BOOLEAN NOT NULL,"skimmer_basket" BOOLEAN NOT NULL,"pump_basket" BOOLEAN NOT NULL,"pump_filter" BOOLEAN NOT NULL)
create table "measurements" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"temp" DOUBLE NOT NULL,"hardness" DOUBLE NOT NULL,"total_chlorine" DOUBLE NOT NULL,"bromine" DOUBLE NOT NULL,"free_chlorine" DOUBLE NOT NULL,"ph" DOUBLE NOT NULL,"alkalinity" DOUBLE NOT NULL,"cyanuric_acid" DOUBLE NOT NULL)
create table "additives" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"chemical" VARCHAR NOT NULL,"unit" VARCHAR NOT NULL,"amount" DOUBLE NOT NULL)
create table "supplies" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"purchased" DATE NOT NULL,"item" VARCHAR NOT NULL,"unit" VARCHAR NOT NULL,"amount" DOUBLE NOT NULL,"cost" DOUBLE NOT NULL)
create table "repairs" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"pool_id" INTEGER NOT NULL,"on" DATE NOT NULL,"item" VARCHAR NOT NULL,"cost" DOUBLE NOT NULL)
alter table "owners" add constraint "pool_owner_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "surfaces" add constraint "pool_surface_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "pumps" add constraint "pool_pump_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "timers" add constraint "pool_timer_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "heaters" add constraint "pool_heater_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "lifecycles" add constraint "pool_lifecycle_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "cleanings" add constraint "pool_cleaning_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "measurements" add constraint "pool_measurement_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "additives" add constraint "pool_additive_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "supplies" add constraint "pool_supply_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION
alter table "repairs" add constraint "pool_repair_fk" foreign key("pool_id") references "pools"("id") on update NO ACTION on delete NO ACTION