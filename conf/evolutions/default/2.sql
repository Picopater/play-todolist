# --- Sample dataset

# --- !Ups
insert into usertask (id,username) values (  0,'guest');
insert into usertask (id,username) values (  1,'usuario1');
insert into usertask (id,username) values (  2,'dmcc');
insert into usertask (id,username) values (  3,'usuario2');

# --- !Downs

delete from task;
delete from usertask;