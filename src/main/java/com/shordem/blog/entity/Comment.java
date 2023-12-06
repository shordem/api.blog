package com.shordem.blog.entity;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends Base implements Serializable {

    @NotNull
    @ManyToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @NotNull
    @ManyToOne
    @MapsId
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Post post;

    @Column(name = "content", nullable = false, length = 512)
    @NotNull
    private String content;

    @ManyToOne
    @JoinColumn(name = "replied_comment_id", nullable = true, table = "comments")
    private Comment repliedComment;

    @OneToMany(mappedBy = "repliedComment")
    private Collection<Comment> replies;
}
