package dev.qrowned.license.server.controller;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.server.repositories.LicenseDataRepository;
import dev.qrowned.license.server.repositories.LicensePlatformRepository;
import dev.qrowned.license.server.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Future<ResponseEntity<LicenseData>> check(@PathVariable UUID platformUUID, @PathVariable UUID licenseKey) {
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
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(this.licenseDataRepository.findAll().stream()
                .map(mongoLicenseData -> (LicenseData) mongoLicenseData)
                .collect(Collectors.toList())));
    }

    @Async
    @GetMapping("{platformUUID}/all")
    public Future<ResponseEntity<List<LicenseData>>> getAll(@PathVariable UUID platformUUID) {
        return this.licenseDataRepository.findAllAsyncByPlatformUUID(platformUUID).thenApplyAsync(ResponseEntity::ok);
    }

    @Async
    @GetMapping("{platformUUID}/allByExtraData/")
    public Future<ResponseEntity<List<LicenseData>>> getAllByExtraData(@PathVariable UUID platformUUID,
                                                                       @RequestParam Object key,
                                                                       @RequestParam Object value) {
        return this.licenseDataRepository.findAllAsyncByPlatformUUID(platformUUID)
                .thenApplyAsync(licenseDataList -> ResponseEntity.ok(
                        licenseDataList.stream()
                                .filter(licenseData -> licenseData.getExtraData().get(key).equals(value))
                                .collect(Collectors.toList())
                ));
    }

    @Async
    @GetMapping("allByExtraData/")
    public Future<ResponseEntity<List<LicenseData>>> getAllByExtraData(@RequestParam Object key,
                                                                       @RequestParam Object value) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(
                this.licenseDataRepository.findAll()
                        .stream()
                        .filter(mongoLicenseData -> mongoLicenseData.getExtraData().get(key).equals(value))
                        .collect(Collectors.toList()))
        );
    }

    @Async
    @PostMapping("create/")
    public Future<ResponseEntity<LicenseData>> create(@RequestParam UUID platformUUID,
                                                      @RequestParam(defaultValue = "-1", required = false) long expirationDate,
                                                      @RequestBody(required = false) Map<Object, Object> extraData) {
        return this.licensePlatformRepository.findAsyncByUuid(platformUUID).thenComposeAsync(licensePlatform -> {
            if (licensePlatform == null) return CompletableFuture.supplyAsync(() -> ResponseEntity.notFound().build());

            return this.licenseService.createLicense(licensePlatform, expirationDate, extraData == null ? new HashMap<>() : extraData)
                    .thenApplyAsync(ResponseEntity::ok);
        });
    }

}
