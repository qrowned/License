package dev.qrowned.license.server.controller.interceptor;

import dev.qrowned.license.server.LicenseServerConfig;
import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public final class RateLimitInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private static final Bandwidth LIMIT = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    private final LicenseServerConfig licenseServerConfig;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        String ipAddress = ServletRequestHelper.getClientIpAddressIfServletRequestExist();
        if (this.licenseServerConfig.getIpWhitelist().contains(ipAddress)) return true;

        Bucket bucket = this.bucketCache.computeIfAbsent(ipAddress, s -> Bucket4j.builder()
                .addLimit(LIMIT)
                .build());

        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        if (consumptionProbe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", Long.toString(consumptionProbe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = consumptionProbe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", Long.toString(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");
            return false;
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/check/**");
    }

}
