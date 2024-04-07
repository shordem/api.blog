package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotNull
    private String content;
}
