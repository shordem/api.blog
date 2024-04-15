package com.shordem.blog.dto;

import lombok.Data;

@Data
public class PostDto {

    private String slug;
    private String title;
    private String content;
    private TagDto[] tags;

}
