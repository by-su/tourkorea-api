create table article
(
    id         bigint auto_increment    primary key,
    title      varchar(500)  not null,
    content    longtext      not null,
    created_at datetime(6)   null,
    created_by bigint        null,
    deleted    bit           not null,
    updated_at datetime(6)   null,
    updated_by bigint        null
);

create table bookmark
(
    id         bigint auto_increment    primary key,
    user_id    bigint      not null,
    article_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint UK_USER_ID_ARTICLE_ID
        unique (user_id, article_id),
    constraint FKcow5ux3yhmj8uwu36so5928gp
        foreign key (article_id) references article (id)
);

create table comment
(
    id         bigint auto_increment    primary key,
    author     varchar(50)  not null,
    content    varchar(500) not null,
    article_id bigint       not null,
    created_at datetime(6)  null,
    created_by bigint       null,
    deleted    bit          not null,
    updated_at datetime(6)  null,
    updated_by bigint       null,
    constraint FK5yx0uphgjc6ik6hb82kkw501y
        foreign key (article_id) references article (id)
);

create table comment_dislike
(
    id         bigint auto_increment    primary key,
    user_id    bigint      not null,
    comment_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint UK_USER_ID_COMMENT_ID
        unique (user_id, comment_id),
    constraint FKoossl2fos69r7gvyr8b3g4cg
        foreign key (comment_id) references comment (id)
);

create table comment_like
(
    id         bigint auto_increment primary key,
    user_id    bigint      not null,
    comment_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint UK_USER_ID_COMMENT_ID
        unique (user_id, comment_id),
    constraint FKqlv8phl1ibeh0efv4dbn3720p
        foreign key (comment_id) references comment (id)
);

create table dislike
(
    id         bigint auto_increment    primary key,
    user_id    bigint      not null,
    article_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint UK_USER_ID_ARTICLE_ID
        unique (user_id, article_id),
    constraint FK5m3ys465qyp8cuns1dum4g17x
        foreign key (article_id) references article (id)
);

create table hashtag
(
    id         bigint auto_increment    primary key,
    tag        varchar(50) not null,
    article_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint FKmcrrtdt7bjprbg6m0rjt22nsf
        foreign key (article_id) references article (id)
);

create table liked
(
    id         bigint auto_increment    primary key,
    user_id    bigint      not null,
    article_id bigint      not null,
    created_at datetime(6) null,
    created_by bigint      null,
    deleted    bit         not null,
    updated_at datetime(6) null,
    updated_by bigint      null,
    constraint UK_USER_ID_ARTICLE_ID
        unique (user_id, article_id),
    constraint FKpkrrlo3fcyj25rixuehgr981y
        foreign key (article_id) references article (id)
);

