package dev.qrowned.license.server.controller;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.server.repositories.LicenseDataRepository;
import dev.qrowned.license.server.repositories.LicensePlatformRepository;
import dev.qrowned.license.server.service.LicenseService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LicenseDataController {

    private final LicenseService licenseService;
    private final LicenseDataRepository licenseDataRepository;
    private final LicensePlatformRepository licensePlatformRepository;

    @Async
    @GetMapping("check/{platformUUID}/{licenseKey}")
    public Future<ResponseEntity<LicenseData>> check(@PathVariable UUID platformUUID,
                                                     @PathVariable UUID licenseKey) {
        ResponseEntity<LicenseData> failedResponse = new ResponseEntity<>(HttpStatus.LOCKED);
        return this.licensePlatformRepository.findAsyncByUuid(platformUUID).thenComposeAsync(licensePlatform -> {
            if (licensePlatform == null) return CompletableFuture.supplyAsync(() -> failedResponse);

            return this.licenseDataRepository.findAsyncByKey(licenseKey).thenApplyAsync(licenseData -> {
                if (licenseData == null || !licenseData.isActive()) return failedResponse;
                return ResponseEntity.ok(licenseData);
            });
        });
    }

    @Async
    @GetMapping("all")
    public Future<ResponseEntity<List<LicenseData>>> getAll() {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(this.licenseDataRepository.findAll()
                .stream().map(mongoLicenseData -> (LicenseData) mongoLicenseData)
                .collect(Collectors.toList())));
    }

    @Async
    @GetMapping("{platformUUID}/all")
    public Future<ResponseEntity<List<LicenseData>>> getAll(@PathVariable UUID platformUUID) {
        return this.licenseDataRepository.findAllAsyncByPlatformUUID(platformUUID).thenApplyAsync(ResponseEntity::ok);
    }

    @Async
    @PostMapping("create")
    public Future<ResponseEntity<LicenseData>> create(@RequestParam UUID platformUUID,
                                                      @RequestParam(defaultValue = "-1", required = false) long expirationDate,
                                                      @RequestParam(required = false) String notice) {
        return this.licensePlatformRepository.findAsyncByUuid(platformUUID).thenComposeAsync(licensePlatform -> {
            if (licensePlatform == null) return CompletableFuture.supplyAsync(() -> ResponseEntity.notFound().build());

            return this.licenseService.createLicense(licensePlatform, expirationDate, notice)
                    .thenApplyAsync(ResponseEntity::ok);
        });
    }

}
