create table "students" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"name" VARCHAR NOT NULL,"born" DATE NOT NULL)
create table "grades" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"student_id" INTEGER NOT NULL,"grade" VARCHAR NOT NULL,"started" DATE NOT NULL,"completed" DATE NOT NULL)
create table "courses" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"grade_id" INTEGER NOT NULL,"name" VARCHAR NOT NULL)
create table "assignments" ("id" INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"course_id" INTEGER NOT NULL,"task" VARCHAR NOT NULL,"assigned" DATE NOT NULL,"completed" DATE NOT NULL,"score" DOUBLE NOT NULL)
alter table "grades" add constraint "student_fk" foreign key("student_id") references "students"("id") on update NO ACTION on delete NO ACTION
alter table "courses" add constraint "grade_fk" foreign key("grade_id") references "grades"("id") on update NO ACTION on delete NO ACTION
alter table "assignments" add constraint "course_fk" foreign key("course_id") references "courses"("id") on update NO ACTION on delete NO ACTION
alter table "assignments" drop constraint "course_fk"
alter table "courses" drop constraint "grade_fk"
alter table "grades" drop constraint "student_fk"