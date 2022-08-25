package dev.qrowned.license.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
