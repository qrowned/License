package dev.qrowned.license.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LicenseData {

    private final UUID platformUUID;

    private final UUID key;
    private final long createdAt;
    private long expirationDate;
    private boolean active;

    @Nullable
    private String notice;

    public boolean isActive() {
        return this.active && (this.expirationDate == -1 || this.expirationDate > System.currentTimeMillis());
    }

}
