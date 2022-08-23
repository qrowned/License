package dev.qrowned.license.server.data;

import dev.qrowned.license.api.data.LicensePlatform;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "license_platforms")
public final class MongoLicensePlatform extends LicensePlatform {

    public MongoLicensePlatform(UUID uuid, String name, long createdAt) {
        super(uuid, name, createdAt);
    }

}
