package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String[] tags;
}
