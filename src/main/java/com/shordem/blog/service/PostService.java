package com.shordem.blog.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.shordem.blog.dto.PostDto;
import com.shordem.blog.dto.TagDto;
import com.shordem.blog.entity.Post;
import com.shordem.blog.entity.Tag;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.PostRepository;
import com.shordem.blog.utils.StringHelper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService extends BaseService<Post> {

    private final PostRepository postRepository;

    private PostDto convertToDto(Post post) {
        Set<Tag> tags = post.getTags();
        TagDto[] tagDtos = tags.stream().map(tag -> new TagDto(tag.getName(), tag.getSlug()))
                .toArray(TagDto[]::new);

        return new PostDto(post.getSlug(), post.getTitle(), post.getContent(), tagDtos);
    }

    private String createSlug(String title) {
        String slug = StringHelper.slugify(title);
        if (postRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }

    @Override
    public void save(Post entity) {
        entity.setSlug(createSlug(entity.getTitle()));

        postRepository.save(entity);
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream().map(this::convertToDto)
                .toList();
    }

    public PostDto findBySlug(String slug) {
        return postRepository.findBySlug(slug).map(this::convertToDto).orElseThrow(() -> new EntityNotFoundException());
    }

    public Post findBySlugEntity(String slug) {
        return postRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException());
    }

    public boolean existsBySlug(String slug) {
        return postRepository.existsBySlug(slug);
    }
}
