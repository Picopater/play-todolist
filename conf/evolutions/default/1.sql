# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255),
    userid integer,
    constraint pk_task primary key (id)
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE usertask (
   id integer NOT NULL DEFAULT nextval('user_id_seq'),
   username varchar(255) NOT NULL,
   constraint pk_user primary key (id),
   constraint uq_username UNIQUE (username)
);

alter table task add constraint fk_task_user_1 foreign key (userid) references usertask (id) on delete restrict on update restrict;
create index ix_task_user_1 on task (userid);
 
# --- !Downs
 
SET REFERENTIAL_INTEGRITY FALSE;
DROP TABLE task;
DROP TABLE usertask;

SET REFERENTIAL_INTEGRITY TRUE;
DROP SEQUENCE task_id_seq;
DROP SEQUENCE user_id_seq;