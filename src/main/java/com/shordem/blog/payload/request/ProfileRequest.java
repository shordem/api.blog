package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Size(max = 500)
    private String bio;

    @Size(max = 50)
    private String website;

    @Size(max = 50)
    private String mail;

    @Size(max = 50)
    private String x;

    @Size(max = 50)
    private String facebook;

    @Size(max = 50)
    private String instagram;

    @Size(max = 100)
    private String avatar;

}
