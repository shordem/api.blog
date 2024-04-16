package com.shordem.blog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.dto.PostDto;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.BookmarkService;
import com.shordem.blog.service.PostService;
import com.shordem.blog.service.UserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookmarks/")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserDetailsService userDetailsService;
    private final PostService postService;

    @PostMapping("/{postSlug}/")
    public ResponseEntity<MessageResponse> addBookmark(@PathVariable("postSlug") String slug) {
        User user = userDetailsService.getAuthenticatedUser();
        Post post = postService.findBySlugEntity(slug);

        Boolean isBookmarked = bookmarkService.isBookmarked(post, user);
        if (isBookmarked) {
            return ResponseEntity.badRequest().body(new MessageResponse("Bookmark already exists"));
        }

        bookmarkService.addBookmark(post, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Bookmark added successfully"));
    }

    @DeleteMapping("/{postSlug}/")
    public ResponseEntity<MessageResponse> removeBookmark(@PathVariable("postSlug") String slug) {
        User user = userDetailsService.getAuthenticatedUser();
        Post post = postService.findBySlugEntity(slug);

        Boolean isBookmarked = bookmarkService.isBookmarked(post, user);
        if (!isBookmarked) {
            return ResponseEntity.badRequest().body(new MessageResponse("Bookmark not found"));
        }

        bookmarkService.removeBookmark(post, user);

        return ResponseEntity.ok().body(new MessageResponse("Bookmark removed successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getBookmarks(Pageable pageable) {
        User user = userDetailsService.getAuthenticatedUser();

        Page<PostDto> bookmarks = bookmarkService.findAllByUser(user, pageable);

        return ResponseEntity.ok().body(bookmarks);
    }
}
