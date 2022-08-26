package dev.qrowned.license.server.controller;

import dev.qrowned.license.api.data.LicensePlatform;
import dev.qrowned.license.server.repositories.LicensePlatformRepository;
import dev.qrowned.license.server.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

    @Async
    @GetMapping("all/")
    public Future<ResponseEntity<List<LicensePlatform>>> getAll() {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(this.licensePlatformRepository.findAll().stream()
                .map(mongoLicensePlatform -> (LicensePlatform) mongoLicensePlatform)
                .collect(Collectors.toList())));
    }

    @Async
    @GetMapping("{name}")
    public Future<ResponseEntity<LicensePlatform>> getPlatform(@PathVariable String name) {
        return this.licensePlatformRepository.findAsyncByName(name)
                .thenApplyAsync(licensePlatform ->
                        licensePlatform == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(licensePlatform)
                );
    }

}
