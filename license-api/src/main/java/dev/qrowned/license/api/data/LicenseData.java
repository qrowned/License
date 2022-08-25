package dev.qrowned.license.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseData {

    private UUID platformUUID;

    private UUID key;
    private long createdAt;
    private long expirationDate;
    private boolean active;

    private Map<Object, Object> extraData = new HashMap<>();

    public boolean isActive() {
        return this.active && (this.expirationDate == -1 || this.expirationDate > System.currentTimeMillis());
    }

}
