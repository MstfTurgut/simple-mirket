package com.mstftrgt.simplemirket.dto;

import com.mstftrgt.simplemirket.domain.Api;
import lombok.Data;



@Data
public class ApiDto {
    private Long id;
    private String name;
    private Api.ApiType type;
    private String method;
    private String targetUrl;
}