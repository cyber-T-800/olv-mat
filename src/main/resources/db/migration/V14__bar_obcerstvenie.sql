create table product
(
    id          uuid                    not null default gen_random_uuid(),
    event_id    uuid                    not null,
    text_id     varchar(50)             not null,

    type        varchar(10)             not null,
    image       bytea                   not null,
    title       varchar(20)             not null,
    description varchar(150),
    price       numeric(2, 1),

    created_at  timestamp default now() not null,
    updated_at  timestamp default now() not null,


    primary key (id),
    constraint fk_product_event foreign key (event_id) references event (id)
);


create table sell_event
(
    id         uuid                    not null default gen_random_uuid(),
    event_id   uuid                    not null,
    product_id uuid                    not null,

    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,

    primary key (id),
    constraint fk_sell_event_event foreign key (event_id) references event(id),
    constraint fk_sell_event_product foreign key (product_id) references product (id)
);