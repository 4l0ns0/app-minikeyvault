create sequence seq_userkey_id start with 1000000 no cycle no cache;
create table userkey (userkey_id int default seq_userkey_id.nextval primary key, application varchar(150), description varchar(200), username varchar(100), password varchar(100));
