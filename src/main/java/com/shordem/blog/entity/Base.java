package com.shordem.blog.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, columnDefinition = "TIMESTAMP")
    private Instant deletedAt;

    @CreatedBy
    @Column(name = "created_by", nullable = true)
    private UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = true)
    private UUID updatedBy;

    @LastModifiedBy
    @Column(name = "deleted_by", nullable = true)
    private UUID deletedBy;

    public Boolean isDeleted() {
        return deletedAt != null;
    }

    public void delete() {
        deletedAt = Instant.now();
    }

    public void restore() {
        deletedAt = null;
    }

}
