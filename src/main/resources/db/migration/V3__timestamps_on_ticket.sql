alter table ticket add column created_at timestamp default now() not null;
alter table ticket add column updated_at timestamp default now() not null;

CREATE OR REPLACE FUNCTION update_timestamp()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ticket_set_updated_at
    BEFORE UPDATE ON ticket
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();
