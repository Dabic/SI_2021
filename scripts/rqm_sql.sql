drop table if exists RQM_ROW;
drop table if exists RQM_DIAGRAM;

create table RQM_DIAGRAM (
	id int primary key auto_increment,
    name varchar(50) not null,
    team_name varchar(50) not null
);

create table RQM_ROW (
	id int primary key auto_increment,
    rqm_id varchar(20),
    header varchar(200),
    description varchar(5000),
    type varchar(100),
    priority varchar(2),
    risk varchar(2),
    status varchar(50),
    rqm_diagram_id int not null,
    foreign key(rqm_diagram_id) references RQM_DIAGRAM(id)
);