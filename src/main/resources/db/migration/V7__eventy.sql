create table if not exists event
(
    id         uuid      default gen_random_uuid() not null,

    nazov      varchar(50)                         not null,
    stav       varchar(20)                         not null default 'UNSELECTED',
    popis      varchar(5000)                       not null,

    created_at timestamp default now()             not null,
    updated_at timestamp default now()             not null,
    primary key (id)
);

insert into event
values ('690eddd9-ba0e-45b8-9002-d666482ada20', 'Rezervácia lístka na OĽV', 'SELECTED', '
            <h1>Rezervácia lístka na OĽV</h1><h1><p style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400;">Švárni šuhaji, driečne devy,</p><div style="color: rgb(0, 0, 0); font-size: medium; font-weight: 400; text-align: start; height: 0.75rem;"></div><p style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400;">Pozývame Vás na Ondrejskú ľudovú veselicu plnú farieb, vzorov a zábavy! Už&nbsp;<strong>28. novembra</strong>&nbsp;bude naše UPeCe znieť ľudovými tancami a piesňami. Ak si toto nechceš nechať ujsť, tu si môžeš zarezervovať lístok. Jednoducho&nbsp;<strong>vyplň formulár</strong>&nbsp;a lístok budeš mať rezervovaný. Následne prídeš do&nbsp;<strong>Libressa</strong>, ktorýkoľvek&nbsp;<strong>pracovný deň 30 minút po svätej omši</strong>, kde nám dáš svoj príspevok a my ti lístok aktivujeme.</p><div style="color: rgb(0, 0, 0); font-size: medium; font-weight: 400; text-align: start; height: 2.25rem;"></div><p style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400;"><strong>Odporúčaný príspevok:</strong></p><pre style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400; text-align: start;">  -  10€  Študent</pre><pre style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400; text-align: start;">  -  20€  Neštudent</pre><div style="color: rgb(0, 0, 0); font-size: medium; font-weight: 400; text-align: start; height: 1.25rem;"></div><p style="font-size: medium; color: rgb(0, 0, 0); font-weight: 400;">Ak máš túžbu pridať ruku k dielu, pridaj sa do kolektívu dobrovoľníkov:</p><a href="https://docs.google.com/forms/d/e/1FAIpQLScQT35Go2tPK3XinxHqm2aahp-Bnjccv7c8_zjW1nsb_9RQKw/viewform?usp=dialog" class="button" style="font-size: medium; font-weight: 400; text-align: start; margin-left: 0.75rem;"><strong>Viac informácií</strong></a><span style="color: rgb(0, 0, 0); font-size: medium; font-weight: 400; text-align: start;"></span></h1><div><br></div>
        ');

alter table ticket
    add column event_id uuid not null default '690eddd9-ba0e-45b8-9002-d666482ada20';
alter table ticket
    add constraint event_id_ref foreign key (event_id) references event (id);