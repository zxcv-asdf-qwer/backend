create sequence account_seq start with 1 increment by 1;
alter table if exists account
    add constraint fk01_account
        foreign key (member_id)
            references member;

create table account
(
    account_id     bigint       not null primary key,
    member_id      varchar(255) not null unique,
    account_name varchar(255) not null,
    account_number varchar(255) not null,
    bank_name varchar(255) not null
);
comment on table account is '메뉴 권한 테이블';
comment on column account.account_id is 'ID';
comment on column account.member_id is 'keycloak ID';
comment on column account.account_name is '계좌 번호';
comment on column account.account_number is '예금주';
comment on column account.bank_name is '은행 이름';
