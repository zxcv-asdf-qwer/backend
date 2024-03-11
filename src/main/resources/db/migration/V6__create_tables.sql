create sequence encrypt_seq start with 1 increment by 1;

create table encrypt_key
(
    encrypt_id     bigint       not null primary key,
    encrypt_type   varchar(20)  not null,
    encrypt_key    varchar(255) not null,
    encrypt_target varchar(20)  not null,
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP
);
comment on table encrypt_key is '암호화 키 테이블';
comment on column encrypt_key.encrypt_id is 'ID';
comment on column encrypt_key.encrypt_type is 'encrypt key type';
comment on column encrypt_key.encrypt_key is 'encrypt key';
comment on column encrypt_key.encrypt_target is 'encrypt target';

create sequence access_key_seq start with 1 increment by 1;
create table access_key2
(
    access_key_id       bigint       not null,
    system_service_type varchar(255) not null,
    service_name        varchar(255) not null,
    access_key          varchar(255),
    expired             timestamp(6),
    created_by          varchar(50),
    created_on          timestamp(6) default CURRENT_TIMESTAMP,
    updated_by          varchar(50),
    updated_on          timestamp(6) default CURRENT_TIMESTAMP,
    primary key (access_key_id)
);
comment on table access_key2 is 'api access key 테이블';
comment on column access_key2.access_key_id is 'ID';
comment on column access_key2.system_service_type is '서비스 타입';
comment on column access_key2.service_name is '서비스 업체 명';
comment on column access_key2.access_key is '서비스 업체 키';
comment on column access_key2.expired is '토큰 만료 시점';