alter table event add column datum date;


create table if not exists schedule_item(
    id uuid default gen_random_uuid() not null,
    event_id uuid default '690eddd9-ba0e-45b8-9002-d666482ada20' not null,
    order_number int default 0 not null,
    length int default 1 not null,
    color varchar(7) default '#ffffff' not null,
    text varchar(100) default '' not null,

    primary key (id),
    foreign key(event_id) references event (id) on delete cascade
);

insert into schedule_item (event_id, order_number, length, color, text)
values
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 0, 60, '#6e8c8c', 'Registrácia'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 1, 60, '#5082d7', 'Sv. omša'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 2, 10, '#87bed2', 'Otvorenie'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 3, 30, '#32affa', 'Tanečné kolo'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 4, 10, '#fafafa', ''),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 5, 30, '#64affa', 'Škola tanca'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 6, 30, '#32affa', 'Tanečné kolo'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 7, 10, '#fafafa', ''),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 8, 20, '#a5bed2', 'Súťaže'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 9, 30, '#32affa', 'Tanečné kolo'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 10, 15, '#87bed2', 'Fotka'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 11, 20, '#a5bed2', 'Limbo'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 12, 35, '#32affa', 'Tanečné kolo'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 13, 30, '#87bed2', 'Tombola'),
    ('690eddd9-ba0e-45b8-9002-d666482ada20', 14, 210, '#64affa', 'Voľná zábava');