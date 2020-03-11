-- 保存sql语句
create table record_info
(
	id          int           auto_increment,
	author      varchar(63)   not null,
	content     varchar(1023) not null,
	time        long          not null,
	constraint record_info
		primary key (id)
);
//创建用户列表
create table user_info
(
    user_name   varchar(20)     not null,
    user_pas    varchar(20)     not null,
    constraint user_info
        primary key (user_name)
);

//创建微博点赞信息表
create table blog_like
(
    id              int             not null ,
    like_user      varchar(20)     not null ,
    constraint id_likes_fk
        foreign key (id) references user_blog(id) ON DELETE CASCADE,
    constraint name_likes_fk
        foreign key (like_user) references user_info(user_name) ON DELETE CASCADE,
    constraint  like_key
        primary key  (id,like_user)
);
//创建微博评论信息表
create table blog_comment
(
    comment_id      int              auto_increment,
    blog_id         int              not null ,
    comment_user    varchar(20)      not null ,
    comment_content varchar(500)     not null ,
    time            long             not null ,
    constraint comment_key
        primary key (comment_id),
    constraint id_comment_fk
        foreign key (blog_id) references user_blog(id) ON DELETE CASCADE,
    constraint name_comment_fk
        foreign key (comment_user)references user_info(user_name) ON DELETE CASCADE
);

//创建微博关注信息表
create table blog_attention
(
    give_User      varchar(20)      not null ,
    get_User       varchar(20)      not null ,
    constraint give_attention_fk
        foreign key (give_User) references user_info(user_name) ON DELETE CASCADE ,
    constraint  get_attention_fk
        foreign key (get_User) references user_info(user_name)ON DELETE CASCADE,
    constraint  attention_key
        primary key  (give_User,get_User)
);