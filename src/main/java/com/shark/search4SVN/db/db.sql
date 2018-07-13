create table t_document(
    id int primary key auto_increment,
    entity_flag char(32) unique not null,
    name varchar(128),
    doc_url varchar(612),
    modify_time datetime,
    description varchar(128)
 );