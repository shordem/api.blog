package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 6, max = 512)
    private String bio;

    @Size(min = 6, max = 40)
    private String website;

    @Size(min = 6, max = 40)
    private String mail;

    @Size(min = 6, max = 40)
    private String x;

    @Size(min = 6, max = 40)
    private String facebook;

    @Size(min = 6, max = 40)
    private String instagram;

    @NotBlank
    private String avatar;

}
