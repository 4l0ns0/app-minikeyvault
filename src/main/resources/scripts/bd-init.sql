create sequence seq_id start with 1000000 no cycle no cache;
create table data (id int default seq_id.nextval primary key, application varchar(150), description varchar(200), username varchar(100), password varchar(500));
