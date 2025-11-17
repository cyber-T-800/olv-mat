alter table admin add primary key (id);
alter table ticket add column schvalil uuid;

alter table ticket
    add constraint fk_ticket_admin
        foreign key (schvalil)
            references admin (id)
                on delete set null ;
