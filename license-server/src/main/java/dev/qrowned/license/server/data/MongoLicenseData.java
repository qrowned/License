package dev.qrowned.license.server.data;

import dev.qrowned.license.api.data.LicenseData;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Document(collection = "license_data")
public final class MongoLicenseData extends LicenseData {

    public MongoLicenseData(UUID platformUUID,
                            UUID key,
                            long createdAt,
                            long expirationDate,
                            boolean active,
                            Map<Object, Object> extraData) {
        super(platformUUID, key, createdAt, expirationDate, active, extraData);
    }
}
