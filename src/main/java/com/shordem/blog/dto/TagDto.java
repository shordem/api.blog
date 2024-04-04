package com.shordem.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDto {
    private String name;
    private String slug;
    private String image;
    private String description;

}
