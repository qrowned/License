package dev.qrowned.license.server.repositories;

import dev.qrowned.license.api.data.LicensePlatform;
import dev.qrowned.license.server.data.MongoLicensePlatform;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface LicensePlatformRepository extends MongoRepository<MongoLicensePlatform, String> {

    @Async
    CompletableFuture<LicensePlatform> findAsyncByUuid(@NotNull UUID uuid);

    @Async
    CompletableFuture<LicensePlatform> findAsyncByName(@NotNull String name);

}
