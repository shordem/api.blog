package com.shordem.blog.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shordem.blog.utils.StringHelper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class Tag extends Base implements Serializable {

    @Column
    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String slug;

    @Column
    @NotNull
    private String description;

    @Column
    private String image;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "posts_tags", joinColumns = @JoinColumn(name = "post_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    private List<Post> posts = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (StringHelper.isEmpty(this.slug)) {
            setSlug(StringHelper.slugify(getName()));
        }

        if (StringHelper.isEmpty(this.image)) {
            setImage("https://ui-avatars.com/api/?name=" + getName());
        }
    }
}
