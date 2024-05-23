create sequence if not exists device_seq start with 1 increment by 1;
create table if not exists device
(
    device_id          bigint       not null primary key,
    phone_type         char(1)      not null,
    device_uuid        varchar(250) not null,
    push_key           varchar(250),
    model_name         varchar(25),
    os_version         varchar(15),
    is_agree_receive   char(1),
    agree_receive_date date,
    member_id          varchar(255) not null,
    created_by         varchar(50),
    created_on         timestamp(6) default CURRENT_TIMESTAMP,
    constraint uk01_device unique (device_uuid)
);
alter table if exists device
    add constraint fk01_member foreign key (member_id) references member;

comment on table device is '앱 버전 체크';
comment on column device.device_id is 'ID';
comment on column device.phone_type is '';
comment on column device.device_uuid is '디바이스 uuid';
comment on column device.push_key is 'push key';
comment on column device.model_name is 'model name';
comment on column device.os_version is 'os version';
comment on column device.is_agree_receive is '수신 여부';
comment on column device.agree_receive_date is 'agree receive date';
comment on column device.member_id is 'member id';

create sequence if not exists push_seq start with 1 increment by 1;
create table if not exists push
(
    push_id     bigint       not null primary key,
    title       varchar(255) not null,
    message     varchar(500),
    send_time   timestamp(6),
    device_uuid varchar(250) not null,
    created_by  varchar(50),
    created_on  timestamp(6) default CURRENT_TIMESTAMP
);

comment on table push is '앱 버전 체크';
comment on column push.push_id is 'ID';
comment on column push.title is '메시지 제목';
comment on column push.message is '메시지 본문';
comment on column push.send_time is '예약된 발송 시간|실제 발송 시간';
comment on column push.device_uuid is 'devic token';
