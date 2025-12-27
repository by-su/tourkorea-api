create table email_token
(
    id    bigint auto_increment primary key,
    email varchar(255) not null,
    link  varchar(255) not null
);

create table user
(
    id                bigint auto_increment     primary key,
    username          varchar(64)   not null,
    email             varchar(64)   not null,
    birth             date          null,
    country_code      varchar(255)  null,
    description       varchar(1000) null,
    password          varchar(500)  null,
    profile_image_url varchar(255)  null,
    created_at        datetime(6)   null,
    updated_at        datetime(6)   null,
    deleted           bit           not null,
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email),
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);

