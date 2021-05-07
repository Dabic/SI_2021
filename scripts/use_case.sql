drop table if exists NODE_FIELD;
drop table if exists D_NODE;
drop table if exists D_LINK;
drop table if exists CLASS_DIAGRAM;

create table CLASS_DIAGRAM (
	id int primary key auto_increment,
    name varchar(50) not null,
    model_type varchar(50) not null,
    team_name varchar(50) not null
);

create table D_NODE (
	id int primary key auto_increment,
    node_key int not null,
    type varchar(50) not null,
    text varchar(50) not null,
    loc varchar(50),
    class_diagram_id int not null,
    foreign key(class_diagram_id) references CLASS_DIAGRAM(id)
);

create table D_LINK (
	id int primary key auto_increment,
    link_key int not null,
    from_node int not null,
    to_node int not null,
    type varchar(50) not null,
    label varchar(50) not null,
    class_diagram_id int not null,
    foreign key(class_diagram_id) references CLASS_DIAGRAM(id)
);

create table NODE_FIELD (
	id int primary key auto_increment,
    node_field int not null,
    text varchar(56) not null,
    info varchar(50) not null,
    type varchar(50) not null,
    d_node_id int not null,
    foreign key(d_node_id) references D_NODE(id)
);