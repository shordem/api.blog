package com.shordem.blog.dto;

import lombok.Data;

@Data
public class TagDto {
    private String name;
    private String slug;

    public TagDto(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
