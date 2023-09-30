package com.shordem.blog.payload.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String message;
}
