package com.shordem.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDto {

    private String slug;
    private String title;
    private String content;
    private TagDto[] tags;

}
