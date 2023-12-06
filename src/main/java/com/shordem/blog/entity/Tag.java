package com.shordem.blog.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shordem.blog.utils.StringHelper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
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
