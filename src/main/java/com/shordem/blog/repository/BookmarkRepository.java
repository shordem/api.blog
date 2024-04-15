package com.shordem.blog.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shordem.blog.entity.Bookmark;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.User;

public interface BookmarkRepository extends PagingAndSortingRepository<Bookmark, UUID> {

    Bookmark findByPostAndUser(Post post, User user);

    boolean existsByPostAndUser(Post post, User user);

    Page<Bookmark> findAllByUser(Pageable pageable, User user);

    void save(Bookmark bookmark);

    void delete(Bookmark bookmark);

}
