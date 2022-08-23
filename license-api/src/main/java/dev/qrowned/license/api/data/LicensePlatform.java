package dev.qrowned.license.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LicensePlatform {

    private final UUID uuid;
    private String name;
    private final long createdAt;

}
