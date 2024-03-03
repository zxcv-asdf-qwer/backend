create table member
(
    member_id               varchar(255) not null primary key,
    user_id                 varchar(150),
    user_pw                 varchar(150),
    user_nm                 varchar(100),
    email                   varchar(150),
    tel_no                  varchar(100),
    gender                  varchar(255),
    use_yn                  char(1)      default 'Y',
    user_type               varchar(10),
    member_register_type    varchar(35),
    jumin1                  varchar(6),
    jumin2                  varchar(7),
    address1                varchar(200),
    address2                varchar(200),
    picture                 varchar(255),
    domestic_foreign_code   char(1),
    career_code             char(1),
    care_start_year         integer,
    introduce               TEXT,
    marketing_app_push_date date,
    marketing_email_date    date,
    marketing_kakao_date    date,
    marketing_sms_date      date,
    deleted_date            date,
    real_name_yn            varchar(255) default 'N',
    created_by              varchar(50),
    created_on              timestamp(6) default CURRENT_TIMESTAMP,
    updated_by              varchar(50),
    updated_on              timestamp(6) default CURRENT_TIMESTAMP,
    constraint uk01_member unique (user_id)
);
comment on table member is '회원 테이블';
comment on column member.member_id is 'Keycloak 의 id';
comment on column member.user_id is '사용자 아이디';
comment on column member.user_pw is '사용자 비밀번호';
comment on column member.user_nm is '사용자 명';
comment on column member.email is '이메일';
comment on column member.tel_no is '전화번호';
comment on column member.gender is '성별';
comment on column member.use_yn is '사용 여부';
comment on column member.user_type is '사용자 구분';
comment on column member.member_register_type is '회원가입 유형';
comment on column member.jumin1 is '주민등록번호 앞자리';
comment on column member.jumin2 is '주민등록번호 뒷자리';
comment on column member.address1 is '주소1';
comment on column member.address2 is '주소2';
comment on column member.picture is '프로필사진 s3 저장소 Path';
comment on column member.domestic_foreign_code is '외국인 내국인';
comment on column member.career_code is '신입 경력';
comment on column member.care_start_year is '근무 시작 연도';
comment on column member.introduce is '자기소개';
comment on column member.marketing_app_push_date is '이메일 수신동의 날짜';
comment on column member.marketing_email_date is '앱 푸시알림 수신동의 날짜';
comment on column member.marketing_kakao_date is '알림톡 수신동의 날짜';
comment on column member.marketing_sms_date is '문자 수신동의 날짜';
comment on column member.deleted_date is '회원 탈퇴 날짜';
comment on column member.real_name_yn is '실명 확인 여부';

alter table if exists system_file
    add constraint fk01_system_file
        foreign key (board_id)
            references board;
CREATE SEQUENCE public.board_seq INCREMENT BY 1 START WITH 1;
create table board
(
    board_id            bigint not null primary key,
    board_type          varchar(10),
    contents_type       varchar(10),
    title               varchar(100),
    small_title         varchar(255),
    contents            varchar(255),
    view_count          integer      default 0,
    use_yn              char(1),
    pin_yn              char(1),
    start_date          timestamp(6),
    end_date            timestamp(6),
    thumbnail_image_url varchar(255),
    created_by          varchar(50),
    created_on          timestamp(6) default CURRENT_TIMESTAMP,
    updated_by          varchar(50),
    updated_on          timestamp(6) default CURRENT_TIMESTAMP
);

comment on table board is '게시판 테이블';
comment on column board.board_id is 'ID';
comment on column board.board_type is '게시판 종류';
comment on column board.contents_type is '컨텐츠 유형';
comment on column board.title is '게시글 제목';
comment on column board.small_title is '소제목';
comment on column board.contents is '게시글 내용';
comment on column board.view_count is '조회수';
comment on column board.use_yn is '게시글 상태';
comment on column board.pin_yn is '상단 고정 여부';
comment on column board.start_date is '시작일';
comment on column board.end_date is '종료일';
comment on column board.thumbnail_image_url is '썸네일 이미지 url';

CREATE SEQUENCE public.hospital_seq INCREMENT BY 1 START WITH 1;
create table hospital
(
    hospital_id              bigint not null primary key,
    hospital_nm              varchar(100),
    hospital_code            varchar(10),
    hospital_address1        varchar(200),
    hospital_address2        varchar(200),
    hospital_tel_no          varchar(100),
    hospital_operation_hours varchar(255)
);

comment on table hospital is '병원정보 테이블';
comment on column hospital.hospital_id is 'ID';
comment on column hospital.hospital_nm is '병원 명';
comment on column hospital.hospital_code is '병원 우편번호';
comment on column hospital.hospital_address1 is '병원 주소';
comment on column hospital.hospital_address2 is '병원 상세 주소';
comment on column hospital.hospital_tel_no is '병원 전화번호';
comment on column hospital.hospital_operation_hours is '병원 운영 시간';

alter table if exists member_group
    add constraint fk01_member_group
        foreign key (member_id)
            references member;
CREATE SEQUENCE public.member_group_seq INCREMENT BY 1 START WITH 1;
create table member_group
(
    member_group_id bigint       not null primary key,
    group_key       varchar(50)  not null,
    group_nm        varchar(150) not null,
    group_path      varchar(250) not null,
    member_id       varchar(255) not null,
    constraint uk01_member_group unique (group_key, member_id)
);

comment on table member_group is '회원 그룹 테이블';
comment on column member_group.group_key is 'Keycloak 의 group ID';
comment on column member_group.member_id is '회원 ID';

alter table if exists menu
    add constraint fk01_menu
        foreign key (parent_id)
            references menu;
CREATE SEQUENCE public.menu_seq INCREMENT BY 1 START WITH 1;
create table menu
(
    menu_id    bigint       not null primary key,
    menu_div   varchar(10)  not null,
    menu_nm    varchar(100) not null,
    menu_url   varchar(150),
    seq        integer,
    menu_type  varchar(6)   not null,
    use_yn     char(1),
    parent_id  bigint,
    created_by varchar(50),
    created_on timestamp(6) default CURRENT_TIMESTAMP,
    updated_by varchar(50),
    updated_on timestamp(6) default CURRENT_TIMESTAMP
);

comment on table menu is '메뉴 테이블';
comment on column menu.menu_id is 'ID';
comment on column menu.menu_div is '메뉴 구분';
comment on column menu.menu_nm is '메뉴명';
comment on column menu.menu_url is '메뉴 URL';
comment on column menu.seq is '메뉴 순서';
comment on column menu.menu_type is '메뉴 타입';
comment on column menu.use_yn is '사용유무';
comment on column menu.parent_id is '상위 메뉴';

alter table if exists menu_permission
    add constraint fk01_menu_permission
        foreign key (member_id)
            references member;
alter table if exists menu_permission
    add constraint fk02_menu_permission
        foreign key (menu_id)
            references menu;
CREATE SEQUENCE public.menu_permission_seq INCREMENT BY 1 START WITH 1;
create table menu_permission
(
    menu_permission_id bigint not null primary key,
    menu_id            bigint,
    member_id          varchar(255),
    group_key          varchar(50),
    created_by         varchar(50),
    created_on         timestamp(6) default CURRENT_TIMESTAMP,
    updated_by         varchar(50),
    updated_on         timestamp(6) default CURRENT_TIMESTAMP
);

comment on table menu_permission is '메뉴 권한 테이블';
comment on column menu_permission.menu_permission_id is 'ID';
comment on column menu_permission.menu_id is '메뉴 ID';
comment on column menu_permission.member_id is '회원 ID';
comment on column menu_permission.group_key is '그룹 ID';

alter table if exists system_file
    add constraint fk01_system_file
        foreign key (board_id)
            references board;
CREATE SEQUENCE public.system_file_seq INCREMENT BY 1 START WITH 1;
create table system_file
(
    system_file_id bigint not null primary key,
    file_path      varchar(255),
    file_nm        varchar(255),
    file_type      varchar(10),
    file_extension varchar(255),
    latest_yn      char(1),
    use_yn         char(1),
    board_id       bigint,
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP
);

comment on table system_file is '메뉴 권한 테이블';
comment on column system_file.system_file_id is 'ID';
comment on column system_file.file_path is '파일 경로';
comment on column system_file.file_nm is '파일 이름';
comment on column system_file.file_type is '파일 타입';
comment on column system_file.file_extension is '파일 확장자명';
comment on column system_file.latest_yn is '최신 여부';
comment on column system_file.use_yn is '사용 여부';
comment on column system_file.board_id is '게시판 ID';

CREATE SEQUENCE public.api_log_seq INCREMENT BY 1 START WITH 1;
create table api_log
(
    log_id bigint not null primary key,
    user_id varchar(255),
    http_method varchar(10),
    request_url varchar(500),
    ip_addr varchar(100),
    created_by         varchar(50),
    created_on         timestamp(6) default CURRENT_TIMESTAMP,
    updated_by         varchar(50),
    updated_on         timestamp(6) default CURRENT_TIMESTAMP
);

comment on table api_log is '로그 테이블';
