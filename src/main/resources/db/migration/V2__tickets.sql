drop table test;

alter table ticket alter column stav set default 'REZERVOVANY';
alter table ticket add column typ_listka varchar(20) default 'STUDENT';
