create sequence if not exists payment_seq start with 1 increment by 1;
create table if not exists payment1
(
    vbank_country_code      varchar(5),
    created_on              timestamp(6) default CURRENT_TIMESTAMP,
    order_id                bigint      not null,
    pay_complete_date       timestamp(6),
    pay_exp_date            timestamp(6),
    payment_id              bigint      not null,
    updated_on              timestamp(6) default CURRENT_TIMESTAMP,
    payment_type            varchar(15),
    vbank_account_name      varchar(30),
    amt                     varchar(50) not null,
    buyer_name              varchar(50),
    buyer_tel               varchar(50),
    buyer_email             varchar(250),
    goods_name              varchar(250),
    moid                    varchar(250),
    pay_request_result_code varchar(250),
    pay_request_result_msg  varchar(250),
    sms_card_order_url      varchar(250),
    vbank_account_tel       varchar(250),
    vbank_bank_code         varchar(250),
    vbank_num               varchar(250),
    vbank_soc_no            varchar(250),
    vbank_tid               varchar(250),
    created_by              varchar(255),
    noti_trans_seq          varchar(255),
    pay_noti_result_code    varchar(255),
    pay_noti_result_msg     varchar(255),
    updated_by              varchar(255),
    primary key (payment_id)
);

alter table if exists payment1
    add constraint fk01_payment
        foreign key (order_id)
            references care_order;