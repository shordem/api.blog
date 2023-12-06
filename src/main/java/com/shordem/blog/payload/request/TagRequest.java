package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    private String description;

}
