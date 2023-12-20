package com.shordem.blog.dto;

import com.shordem.blog.entity.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDto {
    private ERole name;
}