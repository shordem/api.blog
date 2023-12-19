alter table if exists comments
add
    constraint FK44lfn5qjtv4kjiuvwnq6nl0e7 foreign key (created_by) references users;

alter table if exists comments
add
    constraint FK59vpjvskq9e0dfbarx7w57n4j foreign key (deleted_by) references users;

alter table if exists comments
add
    constraint FK8cj6tax2spxdeukbgvkbk5mvv foreign key (updated_by) references users;

alter table if exists likes
add
    constraint FKtgrbntlm65jq9al8yoxqrfj9e foreign key (created_by) references users;

alter table if exists likes
add
    constraint FK4jqamil0i99ubi7mgn7ats8pd foreign key (deleted_by) references users;

alter table if exists likes
add
    constraint FKbvcmh3t61memafkopi4h2qj8j foreign key (updated_by) references users;

alter table if exists posts
add
    constraint FK4s8wtgwpo1h8p5tsy9f04ybjg foreign key (created_by) references users;

alter table if exists posts
add
    constraint FKr2pufx4n0tm911hoqj7kdqgh foreign key (deleted_by) references users;

alter table if exists posts
add
    constraint FKsbta2b48v5gachv43ylk3mli4 foreign key (updated_by) references users;

alter table if exists profiles
add
    constraint FKiaff94dptc6thrqcc7lup4f68 foreign key (created_by) references users;

alter table if exists profiles
add
    constraint FKo5h49x2sa5o9i1jccxauitopf foreign key (deleted_by) references users;

alter table if exists profiles
add
    constraint FKmfmr6vf2ktvwggcqquiumitq7 foreign key (updated_by) references users;

alter table if exists roles
add
    constraint FKq6ium4se7bjk3mfbj3qm1gvy foreign key (created_by) references users;

alter table if exists roles
add
    constraint FKgmr3wlthm6g7ohu0afmpk2jpe foreign key (deleted_by) references users;

alter table if exists roles
add
    constraint FKf0p4aw14esgr0ukams27qfl3m foreign key (updated_by) references users;

alter table if exists tags
add
    constraint FK3sh3rn8hrvjb08s6vu09bldt7 foreign key (created_by) references users;

alter table if exists tags
add
    constraint FKjdomk7i9utck3rv7twppka5ci foreign key (deleted_by) references users;

alter table if exists tags
add
    constraint FKov01u8wfcad49510spj21lmbt foreign key (updated_by) references users;

alter table if exists users
add
    constraint FKibk1e3kaxy5sfyeekp8hbhnim foreign key (created_by) references users;

alter table if exists users
add
    constraint FKtd2l28q3oe9v164nps61hxt1f foreign key (deleted_by) references users;

alter table if exists users
add
    constraint FKci7xr690rvyv3bnfappbyh8x0 foreign key (updated_by) references users;