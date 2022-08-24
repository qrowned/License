package dev.qrowned.license.api.data;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicensePlatform {

    private UUID uuid;
    private String name;
    private long createdAt;

}
