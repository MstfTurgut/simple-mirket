package com.mstftrgt.simplemirket.filter;

import com.mstftrgt.simplemirket.domain.Api;
import com.mstftrgt.simplemirket.domain.ApiRequest;
import com.mstftrgt.simplemirket.repository.ApiRepository;
import com.mstftrgt.simplemirket.repository.ApiRequestRepository;
import com.mstftrgt.simplemirket.service.RateLimitingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.LocalDateTime;


@Component
@Slf4j
public class ApiRedirectGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final ApiRepository apiRepository;
    private final ApiRequestRepository apiRequestRepository;
    private final RateLimitingService rateLimitingService;

    public ApiRedirectGatewayFilterFactory(ApiRepository apiRepository,
                                           ApiRequestRepository apiRequestRepository,
                                           RateLimitingService rateLimitingService) {
        super(Object.class);
        this.apiRepository = apiRepository;
        this.apiRequestRepository = apiRequestRepository;
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            String[] parts = path.split("/");

            if (parts.length < 3) {
                return handleError(exchange, "Invalid path");
            }

            String apiName = parts[2];
            String remainingPath = parts.length > 3 ? "/" + String.join("/", java.util.Arrays.copyOfRange(parts, 3, parts.length)) : "";
            if (!rateLimitingService.tryConsume(apiName)) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }

            return Mono.fromCallable(() -> apiRepository.findByName(apiName))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(apiOpt -> {
                        if (apiOpt.isEmpty()) {
                            return handleError(exchange, "API not found");
                        }

                        Api api = apiOpt.get();
                        String method = exchange.getRequest().getMethod().toString();

                        if (!api.getMethod().equals(method)) {
                            return handleError(exchange, "Method not allowed");
                        }

                        logRequest(api.getId(), remainingPath);

                        try {
                            String queryString = exchange.getRequest().getURI().getRawQuery();
                            String fullUrl = api.getTargetUrl() + remainingPath;
                            if (queryString != null && !queryString.isEmpty()) {
                                fullUrl += "?" + queryString;
                            }

                            URI newUri = new URI(fullUrl);

                            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

                            return chain.filter(exchange)
                                    .doOnSuccess(aVoid -> log.info("Request completed successfully"))
                                    .doOnError(error -> log.error("Request failed", error));

                        } catch (Exception e) {
                            log.error("Error creating redirect URI", e);
                            return handleError(exchange, "Invalid target URL");
                        }
                    });
        }, 10001);
    }

    private void logRequest(Long apiId, String path) {
        Mono.fromRunnable(() -> {
                    ApiRequest request = new ApiRequest();
                    request.setApiId(apiId);
                    request.setPath(path);
                    request.setTimestamp(LocalDateTime.now());
                    apiRequestRepository.save(request);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        null,
                        error -> log.error("Error logging request", error)
                );
    }

    private Mono<Void> handleError(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes()))
        );
    }

    @Override
    public String name() {
        return "ApiRedirect";
    }
}