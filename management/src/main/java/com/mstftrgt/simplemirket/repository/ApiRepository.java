package com.mstftrgt.simplemirket.repository;

import com.mstftrgt.simplemirket.domain.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
    List<Api> findByUserId(Long userId);
    Optional<Api> findByName(String name);
    Optional<Api> findByIdAndUserId(Long id, Long userId);
}