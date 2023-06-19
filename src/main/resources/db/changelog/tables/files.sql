create table files
(
    filename      varchar(255) not null,
    date          datetime(6)  not null,
    file_content  longblob     not null,
    size          bigint       not null,
    user_username varchar(255),
    primary key (filename)
);