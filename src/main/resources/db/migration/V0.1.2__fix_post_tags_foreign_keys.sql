alter table
    if exists posts_tags drop constraint if exists FKmv7kgntmaxhf92embdpp3b3qj;

alter table
    if exists posts_tags drop constraint if exists FK8m204gub9me3xpq546xp52eie;

alter table if exists posts_tags
add
    constraint FKmv7kgntmaxhf92embdpp3b3qj foreign key (post_id) references posts;

alter table if exists posts_tags
add
    constraint FK8m204gub9me3xpq546xp52eie foreign key (tag_id) references tags;