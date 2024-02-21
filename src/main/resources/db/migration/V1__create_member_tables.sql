create table member
(
    member_id      varchar(150) not null constraint pk_ts_member primary key,
    address1             varchar(200),
    address2             varchar(200),
    created_by           varchar(50),
    created_on           timestamp default CURRENT_TIMESTAMP,
    updated_by           varchar(50),
    updated_on           timestamp default CURRENT_TIMESTAMP,
    deleted_date         timestamp default CURRENT_TIMESTAMP,
    dept_cd              varchar(2),
    email                varchar(150),
    login_fail_cnt       integer      default 0,
    marketing_email_date timestamp default CURRENT_TIMESTAMP,
    marketing_kakao_date timestamp default CURRENT_TIMESTAMP,
    marketing_sms_date   timestamp default CURRENT_TIMESTAMP,
    member_register_type varchar(35),
    otp_no               varchar(150),
    otp_yn               varchar(1)   default 'N',
    tel_no               varchar(100),
    use_yn               char         default 'Y',
    user_id              varchar(150)
        constraint uk01_member
            unique,
    user_nm              varchar(100),
    user_nm_en           varchar(100),
    user_pw              varchar(150),
    user_type            varchar(3),
    zipcode              varchar(20)
);