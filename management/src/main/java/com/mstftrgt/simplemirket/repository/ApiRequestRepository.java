package com.mstftrgt.simplemirket.repository;

import com.mstftrgt.simplemirket.domain.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
    List<ApiRequest> findByApiIdOrderByTimestampDesc(Long apiId);
}