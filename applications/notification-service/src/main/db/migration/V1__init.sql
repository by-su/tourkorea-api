create table notification
(
    id         bigint auto_increment    primary key,
    comment    varchar(255)                                                                    not null,
    content_id bigint                                                                          not null,
    read_yn    bit                                                                             null,
    sender_id  bigint                                                                          not null,
    sendername varchar(255)                                                                    not null,
    type       enum ('LIKE_ON_POST', 'LIKE_ON_COMMENT', 'COMMENT_ON_POST', 'REPLY_ON_COMMENT') not null,
    user_id    bigint                                                                          not null,
    username   varchar(255)                                                                    not null,
    created_at datetime(6)                                                                     null,
    created_by bigint                                                                          null,
    deleted    bit                                                                             not null,
    updated_at datetime(6)                                                                     null,
    updated_by bigint                                                                          null,
    constraint UK_USER_ID_SENDER_ID_CONTENT_ID_TYPE
        unique (user_id, sender_id, content_id, type)
);

