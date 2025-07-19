package com.mstftrgt.simplemirket.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long apiId;

    private LocalDateTime timestamp;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apiId", insertable = false, updatable = false)
    private Api api;
}