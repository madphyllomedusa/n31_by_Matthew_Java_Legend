-- SERVICES
create table services
(
    id    varchar(32) primary key,
    title varchar(255) not null,
    price integer      not null,
    image text
);

-- SUBSERVICES
create table subservices
(
    id varchar(32) primary key
);

-- SUBSERVICE TYPES
create table subservice_types
(
    id            varchar(32) primary key,
    title         text,
    image         text,
    service_id    varchar(32) not null,
    subservice_id varchar(32) not null,

    constraint fk_subservice_type_service
        foreign key (service_id) references services (id),

    constraint fk_subservice_type_subservice
        foreign key (subservice_id) references subservices (id)
);

-- SERVICE TYPE EXAMPLES
create table service_type_examples
(
    id      varchar(32) primary key,
    image   varchar(512),
    type_id varchar(32) not null,

    constraint fk_example_type
        foreign key (type_id) references subservice_types (id)
);

-- ABOUT US
create table about_us_text
(
    id        bigserial primary key,
    text      text    not null,
    order_num integer not null
);

create table about_us_stats
(
    id    bigint primary key,
    upper varchar(64),
    lower varchar(128)
);

-- CONTACTS
create table contacts_meta
(
    id          bigserial primary key,
    description text not null
);

create table contacts
(
    id       bigint primary key,
    icon     varchar(64),
    title    varchar(255),
    subtitle varchar(255)
);
