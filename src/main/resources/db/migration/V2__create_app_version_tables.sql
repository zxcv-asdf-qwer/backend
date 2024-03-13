create sequence app_version_seq start with 1 increment by 1;

create table app_version
(
    app_version_id bigint      not null primary key,
    force_update   char(1)     default 'N' check (force_update in ('Y', 'N')),
    last_ver       integer     not null,
    min_ver        integer     not null,
    os_code        varchar(20) not null check (os_code in ('AOS', 'IOS')),
    last_ver_nm    varchar(50) not null,
    min_ver_nm     varchar(50) not null,
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP,
    constraint uk01_app_version unique (os_code, last_ver, min_ver)
);

comment on table app_version is '앱 버전 체크';
comment on column app_version.app_version_id is 'ID';
comment on column app_version.os_code is '요청된 디바이스 os 이름';
comment on column app_version.last_ver is '앱 사용 가능한 최신 버전 정보';
comment on column app_version.last_ver_nm is '앱 사용 가능한 최신 버전 이름';
comment on column app_version.min_ver is '앱 사용 가능한 최소 버전 정보';
comment on column app_version.min_ver_nm is '앱 사용 가능한 최소 버전 이름';
comment on column app_version.force_update is '강제 업데이트 여부';
