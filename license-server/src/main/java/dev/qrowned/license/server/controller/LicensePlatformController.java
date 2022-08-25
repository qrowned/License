package dev.qrowned.license.server.controller;

import dev.qrowned.license.api.data.LicensePlatform;
import dev.qrowned.license.server.repositories.LicensePlatformRepository;
import dev.qrowned.license.server.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/platform/")
@RequiredArgsConstructor
public class LicensePlatformController {

    private final LicenseService licenseService;
    private final LicensePlatformRepository licensePlatformRepository;

    @Async
    @PostMapping("create/")
    public Future<ResponseEntity<LicensePlatform>> create(@RequestParam String name) {
        return this.licensePlatformRepository.findAsyncByName(name).thenComposeAsync(licensePlatform -> {
            if (licensePlatform != null) return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(licensePlatform));

            return this.licenseService.createLicensePlatform(name).thenApplyAsync(ResponseEntity::ok);
        });
    }

}
