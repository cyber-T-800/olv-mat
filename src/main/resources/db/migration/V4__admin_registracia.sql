create table if not exists admin_registracia_ziadost(
    id uuid default gen_random_uuid() not null,
    secret uuid default gen_random_uuid() not null,
    email varchar(50) not null,
    stav varchar(20) default 'PODANA', -- podana - potvrdena/zamietnuta - vybavena
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create table if not exists admin(
    id uuid default gen_random_uuid() not null,
    email varchar(50) not null,
    password varchar(250) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()

);


CREATE TRIGGER admin_registracia_set_updated_at
    BEFORE UPDATE ON admin_registracia_ziadost
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();


CREATE TRIGGER admin_set_updated_at
    BEFORE UPDATE ON admin
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();