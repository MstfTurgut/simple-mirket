package com.mstftrgt.simplemirket.controller;

import com.mstftrgt.simplemirket.domain.User;
import com.mstftrgt.simplemirket.dto.ApiReportDto;
import com.mstftrgt.simplemirket.service.ReportService;
import com.mstftrgt.simplemirket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;

    @GetMapping("/apis/{apiId}")
    public ResponseEntity<ApiReportDto> getApiReport(@PathVariable Long apiId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(reportService.getApiReport(apiId, user));
    }
}