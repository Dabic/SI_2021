drop table if exists ROLES_FOR_USER;
drop table if exists APP_USER;
drop table if exists ROLES_FOR_ENDPOINT;
drop table if exists TEAM;
drop table if exists META_SCHEME_VALUE;
drop table if exists META_SCHEME_KEY;
drop table if exists ENDPOINT;
drop table if exists META_SCHEME;
drop table if exists SERVICE;
drop table if exists USER_ROLE;

create table TEAM (
	id int auto_increment primary key,
    name varchar(50) not null
);

insert into TEAM values (1, "team_1");

create table APP_USER (
	username varchar(45) primary key,
	password varchar(100) not null,
    team_id int,
    foreign key(team_id) references TEAM(id)
);

insert into APP_USER values ("srdjan", "$2y$12$NyOf2b/Qm/i1ZyjFyCSIyuC7xyoLmN2hKmOkAOWKXNfRT8ScBBCMO", 1);
insert into APP_USER values ("isidora1", "$2y$12$NyOf2b/Qm/i1ZyjFyCSIyuC7xyoLmN2hKmOkAOWKXNfRT8ScBBCMO", 1);
insert into APP_USER values ("isidora2", "$2y$12$NyOf2b/Qm/i1ZyjFyCSIyuC7xyoLmN2hKmOkAOWKXNfRT8ScBBCMO", 1);
insert into APP_USER values ("lakistar", "$2y$12$NyOf2b/Qm/i1ZyjFyCSIyuC7xyoLmN2hKmOkAOWKXNfRT8ScBBCMO", 1);
insert into APP_USER values ("admin", "$2y$12$NyOf2b/Qm/i1ZyjFyCSIyuC7xyoLmN2hKmOkAOWKXNfRT8ScBBCMO", null);

create table USER_ROLE (
	name varchar(45) primary key
);

insert into USER_ROLE values ("ADMIN");
insert into USER_ROLE values ("USER");

create table ROLES_FOR_USER (
	id int auto_increment primary key,
	user_id varchar(45) not null,
	role_id varchar(45) not null,
	foreign key(user_id) references APP_USER(username),
	foreign key(role_id) references USER_ROLE(name)
);

insert into ROLES_FOR_USER values (1, "srdjan", "USER");
insert into ROLES_FOR_USER values (2, "isidora1", "USER");
insert into ROLES_FOR_USER values (3, "isidora2", "USER");
insert into ROLES_FOR_USER values (4, "lakistar", "USER");
insert into ROLES_FOR_USER values (5, "admin", "ADMIN");

create table SERVICE (
	name varchar(45) primary key,
    domain varchar(50) not null,
    port int not null
);

create table META_SCHEME (
	id int primary key auto_increment
);

create table META_SCHEME_KEY (
	id int primary key auto_increment,
    name varchar(50) not null,
    meta_scheme_id int not null,
    foreign key(meta_scheme_id) references META_SCHEME(id)
);

create table META_SCHEME_VALUE (
	id int primary key auto_increment,
    name varchar(50) not null,
    meta_scheme_key_id int not null,
    foreign key(meta_scheme_key_id) references META_SCHEME_KEY(id)
);

create table ENDPOINT (
	name varchar(45) primary key,
    method varchar(50) not null,
    path varchar(100) not null,
    service_id varchar(45) not null,
    meta_scheme_id int,
    foreign key(service_id) references SERVICE(name),
    foreign key(meta_scheme_id) references META_SCHEME(id)
);

create table ROLES_FOR_ENDPOINT (
	id int auto_increment primary key,
	endpoint_id varchar(45) not null,
	role_id varchar(45) not null,
	foreign key(endpoint_id) references ENDPOINT(name),
	foreign key(role_id) references USER_ROLE(name)
);
