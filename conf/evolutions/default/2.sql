# --- Sample dataset

# --- !Ups
insert into usertask (id,username) values (0,'guest');
insert into usertask (username) values ('usuario1');
insert into usertask (username) values ('dmcc');
insert into usertask (username) values ('usuario2');

insert into task (label, userid, endate) values ('guest task 1',0,null);
insert into task (label, userid, endate) values ('guest task 2',0,null);
insert into task (label, userid, endate) values ('dmcc task 1',2,null);
insert into task (label, userid, endate) values ('usuario1 task 1',1,null);
insert into task (label, userid, endate) values ('usuario2 task 1',3,null);
insert into task (label, userid, endate) values ('dmcc task with date 1',2,'2014-11-08');
insert into task (label, userid, endate) values ('dmcc task with date 2',2,'2014-11-04');
insert into task (label, userid, endate) values ('guest task with date 1',0,'2012-01-29');

# --- !Downs

delete from task;
delete from usertask;