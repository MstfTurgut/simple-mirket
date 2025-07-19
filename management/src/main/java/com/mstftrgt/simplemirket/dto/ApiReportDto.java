package com.mstftrgt.simplemirket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiReportDto {
    private String apiName;
    private long totalRequests;
    private List<RequestInfoDto> requests;
}