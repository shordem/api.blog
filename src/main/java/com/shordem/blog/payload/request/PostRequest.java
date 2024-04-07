package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String[] tags;
}
