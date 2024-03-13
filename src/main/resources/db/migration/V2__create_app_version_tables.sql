create sequence app_version_seq start with 1 increment by 1;
create table app_version
(
    app_version_id bigint      not null primary key,
    force_update   char(1)      default 'N' check (force_update in ('Y', 'N')),
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
-- comment 넣어주세요