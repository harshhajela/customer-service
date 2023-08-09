create table "customer"
(
    customer_id bigserial   not null,
    email       varchar(50) not null,
    user_name   varchar(50) not null,
    first_name  varchar(50) not null,
    last_name   varchar(50) not null,
    created     timestamp   not null,
    primary key (customer_id)
);