package dev.qrowned.license.server.repositories;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.server.data.MongoLicenseData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface LicenseDataRepository extends MongoRepository<MongoLicenseData, String> {

    @Async
    CompletableFuture<LicenseData> findAsyncByKey(@NotNull UUID key);

    @Async
    @Query("{ 'platformUUID' : ?0 }")
    CompletableFuture<List<LicenseData>> findAllAsyncByPlatformUUID(@NotNull UUID platformUUID);

    @Async
    @Query("{ 'platformUUID' : ?0 }, { 'notice': ?1 }")
    CompletableFuture<List<LicenseData>> findAllAsyncByPlatformUUIDAndNotice(@NotNull UUID platformUUID, @NotNull String notice);

    @Async
    @Query("{ 'notice': ?0 }")
    CompletableFuture<List<LicenseData>> findAllAsyncByNotice(@NotNull String notice);

}
