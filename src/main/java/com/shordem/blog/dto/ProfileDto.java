package com.shordem.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDto {

    private String firstName;
    private String lastName;
    private String bio;
    private String avatar;
    private String website;
    private String mail;
    private String x;
    private String facebook;
    private String instagram;
    private UserDto createdBy;
}
