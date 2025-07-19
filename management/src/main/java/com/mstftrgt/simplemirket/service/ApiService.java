package com.mstftrgt.simplemirket.service;

import com.mstftrgt.simplemirket.domain.Api;
import com.mstftrgt.simplemirket.domain.User;
import com.mstftrgt.simplemirket.dto.ApiDto;
import com.mstftrgt.simplemirket.exception.ResourceNotFoundException;
import com.mstftrgt.simplemirket.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiService {
    private final ApiRepository apiRepository;

    public ApiDto createApi(ApiDto dto, User user) {
        if (apiRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("API name already exists");
        }

        Api api = new Api();
        api.setName(dto.getName());
        api.setType(dto.getType());
        api.setMethod(dto.getMethod());
        api.setTargetUrl(dto.getTargetUrl());
        api.setUserId(user.getId());

        api = apiRepository.save(api);
        return toDto(api);
    }

    public List<ApiDto> getUserApis(User user) {
        return apiRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ApiDto getApi(Long id, User user) {
        Api api = apiRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("API not found"));
        return toDto(api);
    }

    public ApiDto updateApi(Long id, ApiDto dto, User user) {
        Api api = apiRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("API not found"));

        api.setName(dto.getName());
        api.setType(dto.getType());
        api.setMethod(dto.getMethod());
        api.setTargetUrl(dto.getTargetUrl());

        api = apiRepository.save(api);
        return toDto(api);
    }

    public void deleteApi(Long id, User user) {
        Api api = apiRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("API not found"));
        apiRepository.delete(api);
    }

    private ApiDto toDto(Api api) {
        ApiDto dto = new ApiDto();
        dto.setId(api.getId());
        dto.setName(api.getName());
        dto.setType(api.getType());
        dto.setMethod(api.getMethod());
        dto.setTargetUrl(api.getTargetUrl());
        return dto;
    }
}