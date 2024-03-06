alter table public.hospital
    rename column hospital_address1 to hospital_address;

alter table public.hospital
drop column hospital_address2;