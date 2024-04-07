package com.shordem.blog.repository;

import java.util.Optional;
import java.util.UUID;

import com.shordem.blog.entity.Code;

public interface CodeRepository extends BaseRepository<Code, UUID> {

    Optional<Code> findByCodeAndDeletedAtIsNull(String code);

    Boolean existsByCodeAndDeletedAtIsNull(String code);

    Optional<Code> findByUserIdAndDeletedAtIsNull(UUID userId);

}
