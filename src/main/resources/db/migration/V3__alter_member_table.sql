alter table public.member
    rename column deleted_date to leave_date;

alter table public.member
    add leave_reason varchar(255);

comment on column public.member.leave_reason is '탈퇴 사유';

