package com.shordem.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.entity.Tag;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.request.TagRequest;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.TagService;
import com.shordem.blog.service.UserDetailsService;
import com.shordem.blog.utils.StringHelper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tag/")
public class TagController {

    private final TagService tagService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<?> doGetTags() {
        Iterable<Tag> tag = tagService.findAll();
        return ResponseEntity.ok().body(tag);
    }

    @GetMapping("/{slug}/")
    public ResponseEntity<?> doGetTagBySlug(@PathVariable("slug") String slug) {
        Tag tag = tagService.findBySlug(slug);
        return ResponseEntity.ok().body(tag);
    }

    @PostMapping
    public ResponseEntity<?> doCreateTag(@Valid @RequestBody TagRequest tagRequest) {
        String name = tagRequest.getName();
        String description = tagRequest.getDescription();

        User user = userDetailsService.getAuthenticatedUser();

        String slug = StringHelper.slugify(name);

        if (tagService.existsBySlug(slug)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tag is already taken!"));
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        tag.setCreatedBy(user);

        tagService.save(tag);

        return ResponseEntity.ok().body(new MessageResponse("Tag created successfully!"));

    }
}
