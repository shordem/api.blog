package com.shordem.blog.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Base implements Serializable {

    @Column(name = "username", nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String username;

    @Column(name = "email", nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String email;

    @Column(name = "password", nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
