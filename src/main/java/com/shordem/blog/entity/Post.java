package com.shordem.blog.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.shordem.blog.utils.StringHelper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends Base implements Serializable {

    @NotNull
    @ManyToOne
    // @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "posts_tags", joinColumns = @JoinColumn(name = "post_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<Comment> comments = new ArrayList<>();

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String slug;

    @NotNull
    @Column
    private String content;

    @Column(nullable = false)
    protected ZonedDateTime publishOn;

    @PrePersist
    public void preCreate() {
        slugifyIfEmptySlug();
        if (publishOn == null || publishOn.isAfter(ZonedDateTime.now()))
            publishOn = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdateEntity() {
        slugifyIfEmptySlug();
    }

    private void slugifyIfEmptySlug() {
        if (StringHelper.isEmpty(getSlug()))
            setSlug(StringHelper.slugify(getTitle()));

        if (publishOn == null || publishOn.isAfter(ZonedDateTime.now()))
            publishOn = ZonedDateTime.now();
    }
}
