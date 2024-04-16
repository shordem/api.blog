package com.shordem.blog.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shordem.blog.dto.PostDto;
import com.shordem.blog.dto.TagDto;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.Tag;
import com.shordem.blog.entity.User;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.payload.request.PostRequest;
import com.shordem.blog.repository.PostRepository;
import com.shordem.blog.utils.StringHelper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;

    public PostDto convertToDto(Post post) {
        Set<Tag> tags = post.getTags();
        TagDto[] tagDtos = tags.stream()
                .map(tag -> new TagDto(tag.getName(), tag.getSlug(), tag.getImage(), tag.getDescription()))
                .toArray(TagDto[]::new);

        PostDto postDto = new PostDto();
        postDto.setSlug(post.getSlug());
        postDto.setThumbnail(post.getThumbnail());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setTags(tagDtos);

        return postDto;
    }

    private String createSlug(String title) {
        String slug = StringHelper.slugify(title);
        if (postRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }

    public void createBlog(PostRequest postRequest, User user) {
        Post post = new Post();

        String thumbnail = postRequest.getThumbnail();
        String title = postRequest.getTitle();
        String content = postRequest.getContent();
        String[] tags = postRequest.getTags();
        String slug = createSlug(title);
        Set<Tag> tagSet = tagService.findAllBySlugIn(tags);

        post.setSlug(slug);
        post.setThumbnail(thumbnail);
        post.setTitle(title);
        post.setContent(content);
        post.setTags(tagSet);
        post.setCreatedBy(user);

        postRepository.save(post);
    }

    public Page<PostDto> findAll(Pageable pageable) {
        List<Post> posts = postRepository.findAll(pageable).getContent();
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, posts.size());
    }

    public PostDto findBySlug(String slug) {
        return postRepository.findBySlug(slug).map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException(slug + " not found"));
    }

    public Post findBySlugEntity(String slug) {
        return postRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(slug + " not found"));
    }

    public boolean existsBySlug(String slug) {
        return postRepository.existsBySlug(slug);
    }
}
