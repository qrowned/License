package dev.qrowned.license.api.exception;

import java.util.UUID;

public final class LicenseInvalidException extends Exception {

    public LicenseInvalidException(UUID licenseKey) {
        super("The license key " + licenseKey + " is invalid.");
    }

}
