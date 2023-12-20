package com.shordem.blog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.dto.CommentDto;
import com.shordem.blog.dto.PostDto;
import com.shordem.blog.entity.Comment;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.request.CommentRequest;
import com.shordem.blog.payload.request.PostRequest;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.CommentService;
import com.shordem.blog.service.LikeService;
import com.shordem.blog.service.PostService;
import com.shordem.blog.service.TagService;
import com.shordem.blog.service.UserDetailsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/")
public class PostController {

    private final TagService tagService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<?> doGetPosts() {
        return ResponseEntity.ok().body(postService.findAll());
    }

    @GetMapping("/{slug}/")
    public ResponseEntity<?> doGetPostBySlug(@PathVariable("slug") String slug) {
        PostDto post = postService.findBySlug(slug);

        return ResponseEntity.ok().body(post);
    }

    @PostMapping
    public ResponseEntity<?> doCreatePost(@Valid @RequestBody PostRequest postRequest) {
        User user = userDetailsService.getAuthenticatedUser();

        String title = postRequest.getTitle();
        String content = postRequest.getContent();
        String[] tags = postRequest.getTags();

        System.out.println(tagService.findAllBySlugIn(tags) + "these are the tags");

        Post post = new Post();

        post.setTitle(title);
        post.setContent(content);
        post.setTags(tagService.findAllBySlugIn(tags));
        post.setCreatedBy(user);

        postService.save(post);

        return ResponseEntity.ok().body(new MessageResponse("Post created"));
    }

    @PostMapping("/{slug}/comment/")
    public ResponseEntity<?> doCreateComment(@PathVariable("slug") String slug,
            @Valid @RequestBody CommentRequest commentRequest) {
        User user = userDetailsService.getAuthenticatedUser();
        boolean postExists = postService.existsBySlug(slug);

        if (!postExists) {
            return ResponseEntity.badRequest().body(new MessageResponse("Post does not exist"));
        }

        Post post = postService.findBySlugEntity(slug);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(commentRequest.getContent());
        comment.setCreatedBy(user);

        commentService.save(comment);

        return ResponseEntity.ok().body(new MessageResponse("Comment created"));
    }

    @GetMapping("/{slug}/comment/")
    public ResponseEntity<?> doGetComments(@PathVariable("slug") String slug) {
        boolean postExists = postService.existsBySlug(slug);

        if (!postExists) {
            return ResponseEntity.badRequest().body(new MessageResponse("Post does not exist"));
        }

        List<CommentDto> comments = commentService.findAllByPostSlug(slug);

        return ResponseEntity.ok().body(comments);
    }

}
