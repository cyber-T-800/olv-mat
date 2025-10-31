CREATE EXTENSION IF NOT EXISTS pgcrypto;


create table if not exists ticket(
    id uuid default gen_random_uuid() not null,
    security_key uuid default gen_random_uuid() not null,
    email varchar(100) not null,
    meno varchar(100) not null,
    stav varchar(20) not null default 'rezervovany',
    primary key (id)
);

create table if not exists test(
    id serial not null,
    description text,
    primary key (id)
);

insert into test(id, description) values (default, 'funguje');