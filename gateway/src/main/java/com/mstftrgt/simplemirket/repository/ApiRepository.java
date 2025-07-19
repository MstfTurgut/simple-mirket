package com.mstftrgt.simplemirket.repository;


import com.mstftrgt.simplemirket.domain.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
    Optional<Api> findByName(String name);
}