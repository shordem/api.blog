package com.shordem.blog.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends Base implements Serializable {

    @Column(name = "first_name", nullable = false, length = 64)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    @NotNull
    private String lastName;

    @Column(name = "bio", nullable = false, length = 512)
    @NotNull
    private String bio;

    @Column(name = "avatar", nullable = false, length = 128)
    @NotNull
    private String avatar;

    @Column(name = "website", length = 64)
    private String website;

    @Column(name = "mail", length = 64)
    private String mail;

    @Column(name = "x", length = 64)
    private String x;

    @Column(name = "facebook", length = 64)
    private String facebook;

    @Column(name = "instagram", length = 64)
    private String instagram;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;
}
