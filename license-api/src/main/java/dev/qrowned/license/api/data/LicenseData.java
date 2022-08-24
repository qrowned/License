package dev.qrowned.license.api.data;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private String notice;

    public boolean isActive() {
        return this.active && (this.expirationDate == -1 || this.expirationDate > System.currentTimeMillis());
    }

}
