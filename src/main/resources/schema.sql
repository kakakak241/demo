create table if not exists task (
    id bigint auto_increment primary key,
    description varchar(255),
    status varchar(20),
    creation_date timestamp
);
