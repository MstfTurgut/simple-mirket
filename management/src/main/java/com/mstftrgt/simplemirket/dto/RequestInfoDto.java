package com.mstftrgt.simplemirket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoDto {
    private LocalDateTime timestamp;
    private String path;
}