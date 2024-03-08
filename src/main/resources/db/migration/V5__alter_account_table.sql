alter table public.account
    add created_on timestamp;

alter table public.account
    add updated_on timestamp;

alter table public.account
    add created_by varchar(50);

alter table public.account
    add updated_by varchar(50);

