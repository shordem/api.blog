create table
    codes (
        created_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        updated_at TIMESTAMP not null,
        created_by uuid,
        deleted_by uuid,
        id uuid not null,
        user_id uuid not null,
        updated_by uuid,
        code varchar(6) not null,
        primary key (id)
    );

alter table if exists codes
add
    constraint FKjsflk2j33uuiuo489234902fd foreign key (user_id) references users;

alter table if exists codes
add
    constraint FK44lfn5qjtv4kjiuvwnq6nl0e7 foreign key (created_by) references users;

alter table if exists codes
add
    constraint FK59vpjvskq9e0dfbarx7w57n4j foreign key (deleted_by) references users;

alter table if exists codes
add
    constraint FK8cj6tax2spxdeukbgvkbk5mvv foreign key (updated_by) references users;
