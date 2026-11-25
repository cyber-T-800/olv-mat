create table error_log
(
    id         uuid                    not null default gen_random_uuid(),
    message    varchar(200),
    stack_trace text,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,

    primary key (id)
);