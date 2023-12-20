package com.shordem.blog.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.shordem.blog.dto.TagDto;
import com.shordem.blog.entity.Tag;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService extends BaseService<Tag> {

    private final TagRepository tagRepository;

    private TagDto convertToDto(Tag tag) {
        return new TagDto(tag.getName(), tag.getSlug());
    }

    public Boolean existsBySlug(String slug) {
        return tagRepository.findBySlug(slug).isPresent();
    }

    public void save(Tag entity) {
        tagRepository.save(entity);
    }

    public TagDto findBySlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException());
        return convertToDto(tag);
    }

    public Set<Tag> findAllBySlugIn(String[] tags) {
        return tagRepository.findAllBySlugIn(tags);
    }

    public List<TagDto> findAll() {
        return tagRepository.findAllByOrderByCreatedAtDesc().stream().map(this::convertToDto)
                .toList();
    }
}
