package dev.qrowned.license.client;

import dev.qrowned.license.api.data.LicenseData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Getter
public final class LicenseClient {

    private final WebClient client = WebClient.create("https://license.qrowned.dev/");

    public Flux<LicenseData> checkLicenseData(@NotNull UUID platformUUID,
                                              @NotNull UUID licenseKey) {
        WebClient.RequestHeadersUriSpec<?> uriSpec = this.client.get();
        WebClient.RequestHeadersSpec<?> headersSpec = uriSpec.uri("check/" + platformUUID + "/" + licenseKey);
        return headersSpec.exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(LicenseData.class));
    }

}
