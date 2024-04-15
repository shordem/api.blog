package com.shordem.blog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shordem.blog.entity.Bookmark;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.User;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.BookmarkRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public void addBookmark(Post post, User user) {
        Bookmark bookmark = new Bookmark();
        bookmark.setPost(post);
        bookmark.setUser(user);

        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Post post, User user) throws EntityNotFoundException {
        Bookmark bookmark = bookmarkRepository.findByPostAndUser(post, user);
        bookmarkRepository.delete(bookmark);
    }

    public boolean isBookmarked(Post post, User user) {
        return bookmarkRepository.existsByPostAndUser(post, user);
    }

    public Page<Bookmark> findAllByUser(User user, Pageable pageable) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(pageable, user).getContent();

        return new PageImpl<>(bookmarks, pageable, bookmarks.size());
    }
}
