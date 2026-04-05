create table if not exists volunteer (
    id uuid not null primary key,
    name varchar(50) not null ,
    email varchar(50) not null ,
    text varchar(100),
    availability varchar(200),
    services varchar(200)
)