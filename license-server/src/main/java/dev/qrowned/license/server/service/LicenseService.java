package dev.qrowned.license.server.service;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.api.data.LicensePlatform;
import dev.qrowned.license.server.data.MongoLicenseData;
import dev.qrowned.license.server.data.MongoLicensePlatform;
import dev.qrowned.license.server.repositories.LicenseDataRepository;
import dev.qrowned.license.server.repositories.LicensePlatformRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public final class LicenseService {

    private final LicenseDataRepository licenseDataRepository;
    private final LicensePlatformRepository licensePlatformRepository;

    public CompletableFuture<LicensePlatform> createLicensePlatform(@NotNull String name) {
        return CompletableFuture.supplyAsync(() -> this.licensePlatformRepository.save(
                new MongoLicensePlatform(UUID.randomUUID(), name, System.currentTimeMillis()))
        );
    }

    public CompletableFuture<LicenseData> createLicense(@NotNull LicensePlatform licensePlatform,
                                                        long expirationDate,
                                                        Map<Object, Object> extraData) {
        return CompletableFuture.supplyAsync(() -> this.licenseDataRepository.save(
                new MongoLicenseData(
                        licensePlatform.getUuid(),
                        UUID.randomUUID(),
                        System.currentTimeMillis(),
                        expirationDate,
                        true,
                        extraData
                ))
        );
    }

}
