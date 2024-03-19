create sequence inbound_api_seq start with 1 increment by 1;
create table public.inbound_api
(
    inbound_api_id bigint       not null primary key,
    log_type       varchar(10),
    trace_id       varchar(100) not null,
    span_id        varchar(100) not null,
    process_time   bigint       not null,
    service        varchar(50)  not null,
    path           varchar(255) not null,
    method         varchar(6)   not null,
    request_param  text,
    request_data   text,
    response_data  text,
    error          varchar(255),
    user_id        varchar(255),
    remote_address varchar(20)  not null,
    agent          varchar(10),
    created_on     timestamp(6) default CURRENT_TIMESTAMP
);

comment on table inbound_api is '앱 버전 체크';
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
