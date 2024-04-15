CREATE TABLE
    bookmarks (
        created_at TIMESTAMP not null,
        updated_at TIMESTAMP not null,
        deleted_at TIMESTAMP,
        created_by uuid,
        updated_by uuid,
        deleted_by uuid,
        id uuid not null,
        user_id uuid not null,
        post_id uuid not null,
        primary key (id)
    );

ALTER TABLE IF EXISTS bookmarks
ADD
    CONSTRAINT FK_bookmarks_user_id FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS bookmarks
ADD
    CONSTRAINT FK_bookmarks_post_id FOREIGN KEY (post_id) REFERENCES posts;

ALTER TABLE IF EXISTS bookmarks
ADD
    CONSTRAINT FK_bookmarks_created_by FOREIGN KEY (created_by) REFERENCES users;

ALTER TABLE IF EXISTS bookmarks
ADD
    CONSTRAINT FK_bookmarks_updated_by FOREIGN KEY (updated_by) REFERENCES users;

ALTER TABLE IF EXISTS bookmarks
ADD
    CONSTRAINT FK_bookmarks_deleted_by FOREIGN KEY (deleted_by) REFERENCES users;