create table
    comments (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        post_id uuid not null,
        replied_comment_id uuid,
        updated_by uuid,
        content varchar(512) not null,
        primary key (id)
    );

create table
    likes (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        post_id uuid,
        updated_by uuid,
        user_id uuid,
        primary key (id)
    );

create table
    posts (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        publish_on timestamp(6)
        with
            time zone not null,
            updated_at TIMESTAMP not null,
            created_by uuid,
            deleted_by uuid,
            id uuid not null,
            updated_by uuid,
            content varchar(255) not null,
            slug varchar(255) not null,
            title varchar(255) not null,
            primary key (id)
    );

create table
    posts_tags (
        post_id uuid not null,
        tag_id uuid not null,
        primary key (post_id, tag_id)
    );

create table
    profiles (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        updated_by uuid,
        facebook varchar(64),
        first_name varchar(64) not null,
        instagram varchar(64),
        last_name varchar(64) not null,
        mail varchar(64),
        website varchar(64),
        x varchar(64),
        avatar varchar(128) not null,
        bio varchar(512) not null,
        primary key (id)
    );

create table
    roles (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        updated_by uuid,
        name varchar(20) unique check (name in ('USER', 'ADMIN')),
        primary key (id)
    );

create table
    tags (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        updated_by uuid,
        description varchar(255) not null,
        image varchar(255),
        name varchar(255) not null,
        slug varchar(255) not null unique,
        primary key (id)
    );

create table
    user_roles (
        role_id uuid not null,
        user_id uuid not null,
        primary key (role_id, user_id)
    );

create table
    users (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        updated_by uuid,
        email varchar(64) not null unique,
        password varchar(64) not null,
        username varchar(64) not null unique,
        primary key (id)
    );

alter table if exists comments
add
    constraint FKh4c7lvsc298whoyd4w9ta25cr foreign key (post_id) references posts;

alter table if exists comments
add
    constraint FKfo68amw35gbxvjybb9mrxooti foreign key (replied_comment_id) references comments;

alter table if exists likes
add
    constraint FKry8tnr4x2vwemv2bb0h5hyl0x foreign key (post_id) references posts;

alter table if exists likes
add
    constraint FKnvx9seeqqyy71bij291pwiwrg foreign key (user_id) references users;

alter table if exists posts_tags
add
    constraint FKmv7kgntmaxhf92embdpp3b3qj foreign key (tag_id) references posts;

alter table if exists posts_tags
add
    constraint FK8m204gub9me3xpq546xp52eie foreign key (post_id) references tags;

alter table if exists user_roles
add
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles;

alter table if exists user_roles
add
    constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users;