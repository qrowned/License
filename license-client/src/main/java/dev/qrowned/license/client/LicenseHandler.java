package dev.qrowned.license.client;

import dev.qrowned.license.api.data.LicenseData;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RequiredArgsConstructor
public final class LicenseHandler {

    private static final LicenseClient LICENSE_CLIENT = new LicenseClient();

    private final UUID platformUUID;

    public Flux<LicenseData> authenticate(@NotNull UUID licenseKey) {
        return LICENSE_CLIENT.checkLicenseData(this.platformUUID, licenseKey);
    }

}
