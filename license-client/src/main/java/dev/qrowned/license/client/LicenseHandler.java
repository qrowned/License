package dev.qrowned.license.client;

import dev.qrowned.license.api.data.LicenseData;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public final class LicenseHandler {

    private static final LicenseClient LICENSE_CLIENT = new LicenseClient();

    private final UUID platformUUID;

    public LicenseData authenticate(@NotNull UUID licenseKey) throws IOException {
        return LICENSE_CLIENT.retrieveLicenseData(this.platformUUID, licenseKey);
    }

}
