create sequence if not exists access_key_seq start with 1 increment by 1;
create table if not exists access_key
(
    access_key_id       bigint       not null,
    system_service_type varchar(255) not null,
    service_name        varchar(255) not null,
    access_key          text,
    expired             timestamp(6),
    created_by          varchar(50),
    created_on          timestamp(6) default CURRENT_TIMESTAMP,
    updated_by          varchar(50),
    updated_on          timestamp(6) default CURRENT_TIMESTAMP,
    primary key (access_key_id)
);
comment on table access_key is 'api access key 테이블';
comment on column access_key.access_key_id is 'ID';
comment on column access_key.system_service_type is '서비스 타입';
comment on column access_key.service_name is '서비스 업체 명';
comment on column access_key.access_key is '서비스 업체 키';
comment on column access_key.expired is '토큰 만료 시점';

create sequence if not exists account_seq start with 1 increment by 1;
create table if not exists account
(
    account_id     bigint       not null primary key,
    member_id      varchar(255) not null unique,
    account_name   varchar(255) not null,
    account_number varchar(255) not null,
    bank_name      varchar(255) not null,
    pass_book_url  varchar(255),
    iv             varchar(255) not null,
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP
);
comment on table account is '메뉴 권한 테이블';
comment on column account.account_id is 'ID';
comment on column account.member_id is 'keycloak ID';
comment on column account.account_name is '계좌 번호';
comment on column account.account_number is '예금주';
comment on column account.bank_name is '은행 이름';
comment on column account.iv is 'initial vector';

create sequence if not exists answer_seq start with 1 increment by 1;
create table if not exists answer
(
    answer_id      bigint not null primary key,
    question_id    bigint not null unique,
    answer_title   varchar(255),
    answer_content varchar(255),
    use_yn         char(1),
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP
);

comment on table answer is '1:1 문의 답변 테이블';
comment on column answer.answer_id is 'ID';
comment on column answer.question_id is '질문 ID';
comment on column answer.answer_title is '답변 타이틀';
comment on column answer.answer_content is '답변 내용';
comment on column answer.use_yn is '답변 상태';

create sequence if not exists app_version_seq start with 1 increment by 1;

create table if not exists app_version
(
    app_version_id bigint      not null primary key,
    force_update   char(1)      default 'N',
    os_code        varchar(20) not null,
    min_ver        varchar(50) not null,
    update_url     varchar(50) not null,
    contents       varchar(255),
    created_by     varchar(50),
    created_on     timestamp(6) default CURRENT_TIMESTAMP,
    updated_by     varchar(50),
    updated_on     timestamp(6) default CURRENT_TIMESTAMP,
    constraint uk01_app_version unique (os_code, min_ver)
);

comment on table app_version is '앱 버전 체크';
comment on column app_version.app_version_id is 'ID';
comment on column app_version.os_code is '요청된 디바이스 os 이름';
comment on column app_version.min_ver is '앱 사용 가능한 최소 버전 정보';
comment on column app_version.force_update is '강제 업데이트 여부';
comment on column app_version.contents is '내용';

create sequence if not exists apply_seq start with 1 increment by 1;
create table if not exists apply
(
    apply_id      bigint       not null,
    care_order_id bigint       not null,
    apply_status  varchar(10),
    member_id     varchar(255) not null,
    created_by    varchar(50),
    created_on    timestamp(6) default CURRENT_TIMESTAMP,
    updated_by    varchar(50),
    updated_on    timestamp(6) default CURRENT_TIMESTAMP,
    primary key (apply_id)
);

comment on table apply is '지원한 간병인';
comment on column apply.apply_id is 'ID';
comment on column apply.care_order_id is '공고 ID';
comment on column apply.apply_status is '지원 상태';
comment on column apply.member_id is '앱 사용 가능한 최신 버전 이름';

create sequence if not exists public.board_seq INCREMENT BY 1 START WITH 1;
create table if not exists board
(
    board_id            bigint not null primary key,
    board_type          varchar(10),
    contents_type       varchar(10),
    title               varchar(100),
    small_title         varchar(255),
    contents            text,
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

create sequence if not exists care_order_seq start with 1 increment by 1;
create table if not exists care_order
(
    care_order_id           bigint not null primary key,
    end_date_time           timestamp(6),
    real_end_date_time      timestamp(6),
    order_patient_id        bigint not null,
    start_date_time         timestamp(6),
    care_order_process_type varchar(15),
    member_id               varchar(255),
    order_request           TEXT,
    order_status            varchar(15)  default 'MATWAI',
    order_type              varchar(10)  default 'GENERAL',
    publish_yn              varchar(255),
    title                   varchar(255),
    created_by              varchar(50),
    created_on              timestamp(6) default CURRENT_TIMESTAMP,
    updated_by              varchar(50),
    updated_on              timestamp(6) default CURRENT_TIMESTAMP
);

create sequence if not exists encrypt_seq start with 1 increment by 1;
create table if not exists encrypt_key
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

create sequence if not exists inbound_api_seq start with 1 increment by 1;
create table if not exists public.inbound_api
(
    inbound_api_id bigint       not null primary key,
    log_type       varchar(10),
    trace_id       varchar(100) not null,
    span_id        varchar(100) not null,
    process_time   bigint       not null,
    service        varchar(50)  not null,
    path           text         not null,
    method         varchar(6)   not null,
    request_param  text,
    request_data   text,
    response_data  text,
    error          text,
    user_id        varchar(255),
    remote_address varchar(20)  not null,
    agent          varchar(10),
    created_on     timestamp(6) default CURRENT_TIMESTAMP
);

comment on table inbound_api is 'api 로그';
comment on column inbound_api.inbound_api_id is 'ID';
comment on column inbound_api.log_type is 'log type';
comment on column inbound_api.trace_id is 'trace id';
comment on column inbound_api.span_id is 'span id';
comment on column inbound_api.process_time is 'process time';
comment on column inbound_api.service is 'service';
comment on column inbound_api.path is 'path';
comment on column inbound_api.method is 'method';
comment on column inbound_api.request_param is 'request param';
comment on column inbound_api.request_data is 'request data';
comment on column inbound_api.response_data is 'response data';
comment on column inbound_api.error is 'error';
comment on column inbound_api.user_id is 'user id';
comment on column inbound_api.remote_address is 'Ip address';
comment on column inbound_api.agent is 'agent';

create sequence if not exists public.member_group_seq INCREMENT BY 1 START WITH 1;
create table if not exists member_group
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

create sequence if not exists member_seq start with 1 increment by 1;
create table if not exists member
(
    member_id                  varchar(255) not null primary key,
    user_id                    varchar(150),
    user_pw                    varchar(150),
    user_nm                    varchar(100),
    email                      varchar(150),
    tel_no                     varchar(100),
    gender                     varchar(255),
    use_yn                     char(1)      default 'Y',
    user_type                  varchar(10),
    member_type                varchar(10)  default 'MEMBER',
    member_register_type       varchar(35),
    jumin1                     varchar(6),
    jumin2                     varchar(7),
    address1                   varchar(200),
    address2                   varchar(200),
    picture                    varchar(255),
    domestic_foreign_code      varchar(10),
    career_code                varchar(5),
    care_start_year            integer,
    introduce                  TEXT,
    marketing_app_push_date    date,
    marketing_email_date       date,
    marketing_kakao_date       date,
    marketing_sms_date         date,
    leave_reason               varchar(255),
    leave_date                 date,
    real_name_yn               varchar(255) default 'N',
    dept_code                  varchar(35),
    ci                         varchar(255),
    di                         varchar(255),
    recent_login_date          timestamp(6),
    ip_address                 varchar(255),
    disease_nms                json,
    self_toilet_availabilities json,
    created_by                 varchar(50),
    created_on                 timestamp(6) default CURRENT_TIMESTAMP,
    updated_by                 varchar(50),
    updated_on                 timestamp(6) default CURRENT_TIMESTAMP,
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
comment on column member.member_type is '비회원 구분';
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
comment on column member.leave_reason is '탈퇴 사유';
comment on column member.leave_date is '회원 탈퇴 날짜';
comment on column member.real_name_yn is '실명 확인 여부';
comment on column member.dept_code is '부서코드';
comment on column member.ci is 'ci';
comment on column member.di is 'di';
comment on column member.recent_login_date is '최종 접속 일시';
comment on column member.ip_address is '접속 ip';

create sequence if not exists public.menu_permission_seq INCREMENT BY 1 START WITH 1;
create table if not exists menu_permission
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

create sequence if not exists public.menu_seq INCREMENT BY 1 START WITH 1;
create table if not exists menu
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

create sequence if not exists order_patient_seq start with 1 increment by 1;
create table if not exists order_patient
(
    order_patient_id           bigint not null,
    birth_date                 date,
    covid19test                char(1),
    gender                     char(1),
    gender_preference          char(1),
    height                     integer,
    move_availability          char(1),
    weight                     integer,
    meal_availability          varchar(10),
    name                       varchar(50),
    address1                   varchar(200),
    address2                   varchar(200),
    hospital_name              varchar(100),
    location_type              varchar(255),
    member_id                  varchar(255),
    patient_request            varchar(255),
    disease_nms                json,
    self_toilet_availabilities json,
    created_by                 varchar(50),
    created_on                 timestamp(6) default CURRENT_TIMESTAMP,
    updated_by                 varchar(50),
    updated_on                 timestamp(6) default CURRENT_TIMESTAMP,
    primary key (order_patient_id)
);

create sequence if not exists packing_seq start with 1 increment by 1;
create table if not exists packing
(
    packing_id      bigint  not null,
    amount          integer not null,
    care_order_id   bigint  not null,
    end_date_time   timestamp(6),
    settle_id       bigint  not null,
    start_date_time timestamp(6),
    period_type     varchar(255),
    part_time       integer,
    created_by      varchar(50),
    created_on      timestamp(6) default CURRENT_TIMESTAMP,
    updated_by      varchar(50),
    updated_on      timestamp(6) default CURRENT_TIMESTAMP,
    primary key (packing_id)
);

create sequence if not exists patient_seq start with 1 increment by 1;
create table if not exists patient
(
    patient_id                 bigint not null,
    birth_date                 date,
    covid19test                char(1),
    gender                     char(1),
    gender_preference          char(1),
    height                     integer,
    move_availability          char(1),
    weight                     integer,
    meal_availability          varchar(10),
    name                       varchar(50),
    address1                   varchar(200),
    address2                   varchar(200),
    hospital_name              varchar(100),
    location_type              varchar(255),
    member_id                  varchar(255),
    patient_request            varchar(255),
    disease_nms                json,
    self_toilet_availabilities json,
    created_by                 varchar(50),
    created_on                 timestamp(6) default CURRENT_TIMESTAMP,
    updated_by                 varchar(50),
    updated_on                 timestamp(6) default CURRENT_TIMESTAMP,
    primary key (patient_id)
);

create sequence if not exists payment_cancel_seq start with 1 increment by 1;
create table if not exists payment_cancel
(
    payment_cancel_id bigint not null,
    payment_id        bigint not null,
    created_by        varchar(50),
    created_on        timestamp(6) default CURRENT_TIMESTAMP,
    updated_by        varchar(50),
    updated_on        timestamp(6) default CURRENT_TIMESTAMP,
    primary key (payment_cancel_id)
);

create sequence if not exists payment_seq start with 1 increment by 1;
create table if not exists payment
(

    payment_id              bigint  not null,
    order_id                bigint  not null,
    goods_name              varchar(250),
    price                   integer not null,
    moid                    varchar(255),
    payment_type            varchar(15),
    order_url               varchar(250),
    buyer_name              varchar(250),
    buyer_tel               varchar(250),
    buyer_email             varchar(250),
    pay_exp_date            timestamp(6),
    pay_request_result_code varchar(255),
    created_by              varchar(50),
    created_on              timestamp(6) default CURRENT_TIMESTAMP,
    updated_by              varchar(50),
    updated_on              timestamp(6) default CURRENT_TIMESTAMP,
    primary key (payment_id)
);

create sequence if not exists question_seq start with 1 increment by 1;
create table if not exists question
(
    question_id      bigint not null primary key,
    question_type    varchar(255),
    question_title   varchar(255),
    question_content varchar(255),
    is_answer        char(1),
    use_yn           char(1),
    created_by       varchar(50),
    created_on       timestamp(6) default CURRENT_TIMESTAMP,
    updated_by       varchar(50),
    updated_on       timestamp(6) default CURRENT_TIMESTAMP
);

comment on table question is '1:1 문의 질문 테이블';
comment on column question.question_id is 'ID';
comment on column question.question_type is '질문 유형';
comment on column question.question_title is '질문 타이틀';
comment on column question.question_content is '질문 내용';
comment on column question.is_answer is '답변 유무';
comment on column question.use_yn is '질문 상태';

create sequence if not exists settle_seq start with 1 increment by 1;
create table if not exists settle
(
    settle_id     bigint not null,
    guardian_fees integer,
    partner_fees  integer,
    use_yn        varchar(255),
    created_by    varchar(50),
    created_on    timestamp(6) default CURRENT_TIMESTAMP,
    primary key (settle_id)
);

create sequence if not exists sms_seq start with 1 increment by 1;
create table if not exists sms
(
    sms_id                bigint not null primary key,
    member_id             varchar(255),
    receiver_phone_number varchar(100),
    sender_phone_number   varchar(100),
    contents              text,
    refkey                varchar(255),
    sendtime              timestamp(6),
    info_template_id      bigint,
    result_code           varchar(10),
    fail_cause            text,
    created_by            varchar(50),
    created_on            timestamp(6) default CURRENT_TIMESTAMP,
    updated_by            varchar(50),
    updated_on            timestamp(6) default CURRENT_TIMESTAMP
);
comment on table sms is 'api access key 테이블';
comment on column sms.sms_id is 'ID';
comment on column sms.member_id is '회원 ID';
comment on column sms.sender_phone_number is '보내는 전화번호';
comment on column sms.receiver_phone_number is '받는 전화번호';
comment on column sms.contents is '내용';
comment on column sms.refkey is '비즈뿌리오에 보내는 unique 값';
comment on column sms.sendtime is '발송시간';
comment on column sms.info_template_id is 'sms 템플릿 ID';
comment on column sms.result_code is '결과코드';
comment on column sms.fail_cause is '실패사유';

create sequence if not exists info_template_seq start with 1 increment by 1;
create table if not exists info_template
(
    info_template_id   bigint     not null primary key,
    info_template_type varchar(3) not null,
    template_code      varchar(50),
    contents           text       not null,
    created_by         varchar(50),
    created_on         timestamp(6) default CURRENT_TIMESTAMP,
    updated_by         varchar(50),
    updated_on         timestamp(6) default CURRENT_TIMESTAMP
);

comment on table info_template is 'sms 템플릿 테이블';
comment on column info_template.info_template_id is 'ID';
comment on column info_template.info_template_type is 'SMS 템플릿 종류';
comment on column info_template.template_code is '카카오 알림톡 템플릿 코드';
comment on column info_template.contents is '내용';

create sequence if not exists public.system_file_seq INCREMENT BY 1 START WITH 1;
create table if not exists system_file
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

create sequence if not exists terms_seq start with 1 increment by 1;
create table if not exists terms
(
    terms_id   bigint not null,
    terms_type varchar(10),
    contents   text,
    use_yn     char(1),
    created_by varchar(50),
    created_on timestamp(6) default CURRENT_TIMESTAMP,
    updated_by varchar(50),
    updated_on timestamp(6) default CURRENT_TIMESTAMP,
    primary key (terms_id)
);

create sequence if not exists facking_seq start with 1 increment by 1;
create table if not exists facking
(
    facking_id      bigint  not null,
    care_order_id   bigint  not null,
    settle_id       bigint  not null,
    amount          integer not null,
    start_date_time timestamp(6),
    end_date_time   timestamp(6),
    partner_nm      varchar(100),
    partner_tel_no  varchar(100),
    location_type   varchar(10),
    address1        varchar(200),
    address2        varchar(200),
    period_type     varchar(255),
    part_time       integer,
    created_by      varchar(50),
    created_on      timestamp(6) default CURRENT_TIMESTAMP,
    updated_by      varchar(50),
    updated_on      timestamp(6) default CURRENT_TIMESTAMP,
    primary key (facking_id)
);

create sequence if not exists memo_seq start with 1 increment by 1;
create table if not exists memo
(
    care_order_id bigint not null,
    memo_id       bigint not null,
    contents      text,
    created_by    varchar(50),
    created_on    timestamp(6) default CURRENT_TIMESTAMP,
    updated_by    varchar(50),
    updated_on    timestamp(6) default CURRENT_TIMESTAMP,
    primary key (memo_id)
);

create sequence if not exists review_seq start with 1 increment by 1;
create table if not exists review
(
    review_id     bigint       not null,
    care_order_id bigint       not null,
    member_id     varchar(255) not null,
    point         integer      not null,
    publish       char(1),
    contents      text,
    created_by    varchar(50),
    created_on    timestamp(6) default CURRENT_TIMESTAMP,
    updated_by    varchar(50),
    updated_on    timestamp(6) default CURRENT_TIMESTAMP,
    primary key (review_id)
);

create sequence if not exists report_seq start with 1 increment by 1;
create table if not exists report
(
    report_id   bigint not null,
    review_id   bigint not null,
    report_type varchar(10),
    contents    text,
    created_by  varchar(50),
    created_on  timestamp(6) default CURRENT_TIMESTAMP,
    updated_by  varchar(50),
    updated_on  timestamp(6) default CURRENT_TIMESTAMP,
    primary key (report_id)
);

alter table if exists account
    add constraint fk01_account
        foreign key (member_id)
            references member
;
alter table if exists answer
    add constraint f01_answer
        foreign key (question_id)
            references question
;
alter table if exists apply
    add constraint fk01_apply
        foreign key (care_order_id)
            references care_order
;
alter table if exists apply
    add constraint fk02_apply
        foreign key (member_id)
            references member
;
alter table if exists care_order
    add constraint fk01_care_order
        foreign key (member_id)
            references member
;
alter table if exists care_order
    add constraint fk03_care_order
        foreign key (order_patient_id)
            references order_patient
;
alter table if exists member_group
    add constraint fk01_member_group
        foreign key (member_id)
            references member
;
alter table if exists menu
    add constraint fk01_menu
        foreign key (parent_id)
            references menu
;
alter table if exists menu_permission
    add constraint fk01_menu_permission
        foreign key (member_id)
            references member
;
alter table if exists menu_permission
    add constraint fk02_menu_permission
        foreign key (menu_id)
            references menu
;
alter table if exists order_patient
    add constraint fk01_order_patient
        foreign key (member_id)
            references member
;

alter table if exists packing
    add constraint fk01_packing
        foreign key (care_order_id)
            references care_order
;
alter table if exists packing
    add constraint fk02_packing
        foreign key (settle_id)
            references settle
;
alter table if exists patient
    add constraint fk01_patient
        foreign key (member_id)
            references member
;
alter table if exists payment
    add constraint fk01_payment
        foreign key (order_id)
            references care_order
;
alter table if exists payment_cancel
    add constraint fk01_payment_cancel
        foreign key (payment_id)
            references payment
;
alter table if exists sms
    add constraint fk01_sms
        foreign key (info_template_id)
            references info_template
;
alter table if exists system_file
    add constraint fk01_system_file
        foreign key (board_id)
            references board
;

alter table if exists facking
    add constraint fk01_facking
        foreign key (care_order_id)
            references care_order
;
alter table if exists facking
    add constraint fk02_facking
        foreign key (settle_id)
            references settle
;
alter table if exists memo
    add constraint fk01_memo
        foreign key (care_order_id)
            references care_order
;
alter table if exists report
    add constraint fk01_report
        foreign key (review_id)
            references review
;
alter table if exists review
    add constraint fk01_review
        foreign key (care_order_id)
            references care_order
;
alter table if exists review
    add constraint fk02_review
        foreign key (member_id)
            references member;

CREATE TABLE IF NOT EXISTS shedlock
(
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL,
    locked_by  VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);