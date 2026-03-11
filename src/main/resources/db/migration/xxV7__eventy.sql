create table if not exist events(
    id uuid default gen_random_uuid() not null,
    security_key uuid default gen_random_uuid() not null,
    email varchar(100) not null,
    meno varchar(100) not null,
    stav varchar(20) not null default 'rezervovany',
    alter table ticket add column created_at timestamp default now() not null;
    alter table ticket add column updated_at timestamp default now() not null;
    primary key (id)
);