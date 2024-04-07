package com.shordem.blog.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyOtpRequest {

    @NotBlank
    private String code;
}
