package dev.qrowned.license.server.repositories;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.server.data.MongoLicenseData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface LicenseDataRepository extends MongoRepository<MongoLicenseData, String> {

    @Async
    CompletableFuture<LicenseData> findAsyncByKey(@NotNull UUID key);

}
