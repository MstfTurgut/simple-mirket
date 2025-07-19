package com.mstftrgt.simplemirket.service;

import com.mstftrgt.simplemirket.config.RateLimitingConfig;
import com.mstftrgt.simplemirket.repository.ApiRepository;
import com.mstftrgt.simplemirket.repository.UserRepository;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.mstftrgt.simplemirket.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitingService {
    private final RateLimitingConfig rateLimitingConfig;
    private final ApiRepository apiRepository;
    private final UserRepository userRepository;

    public boolean tryConsume(String apiName) {
        Bucket bucket = rateLimitingConfig.getBucket(apiName);
        boolean consumed = bucket.tryConsume(1);

        if (!consumed) {
            notifyOwner(apiName);
        }

        return consumed;
    }

    private void notifyOwner(String apiName) {
        apiRepository.findByName(apiName).ifPresent(api -> {
            User owner = userRepository.findById(api.getUserId()).orElse(null);
            if (owner != null) {
                log.info("Email sent to {}: Rate limit exceeded for API '{}'", owner.getEmail(), apiName);
            }
        });
    }
}