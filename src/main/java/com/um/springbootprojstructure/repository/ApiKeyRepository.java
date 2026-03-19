package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    List<ApiKey> findAllByUserIdOrderByIdDesc(Long userId);

    Optional<ApiKey> findByIdAndUserId(Long id, Long userId);
}