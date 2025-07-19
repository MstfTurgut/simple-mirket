package com.mstftrgt.simplemirket.repository;

import com.mstftrgt.simplemirket.domain.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
}