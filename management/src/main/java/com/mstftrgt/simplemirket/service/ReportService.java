package com.mstftrgt.simplemirket.service;

import com.mstftrgt.simplemirket.domain.Api;
import com.mstftrgt.simplemirket.domain.ApiRequest;
import com.mstftrgt.simplemirket.domain.User;
import com.mstftrgt.simplemirket.dto.ApiReportDto;
import com.mstftrgt.simplemirket.dto.RequestInfoDto;
import com.mstftrgt.simplemirket.exception.ResourceNotFoundException;
import com.mstftrgt.simplemirket.repository.ApiRepository;
import com.mstftrgt.simplemirket.repository.ApiRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ApiRepository apiRepository;
    private final ApiRequestRepository apiRequestRepository;

    public ApiReportDto getApiReport(Long apiId, User user) {
        Api api = apiRepository.findByIdAndUserId(apiId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("API not found"));

        List<ApiRequest> requests = apiRequestRepository.findByApiIdOrderByTimestampDesc(apiId);

        List<RequestInfoDto> requestInfos = requests.stream()
                .map(req -> new RequestInfoDto(
                        req.getTimestamp(),
                        req.getPath()
                ))
                .collect(Collectors.toList());

        return new ApiReportDto(
                api.getName(),
                requests.size(),
                requestInfos
        );
    }
}