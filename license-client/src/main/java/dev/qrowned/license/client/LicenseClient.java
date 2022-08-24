package dev.qrowned.license.client;

import dev.qrowned.license.api.data.LicenseData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import com.google.gson.*;

@Getter
public final class LicenseClient {

    private static final String URL = "https://license.qrowned.dev/";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public LicenseData retrieveLicenseData(@NotNull UUID platformUUID,
                                        @NotNull UUID licenseKey) throws IOException {
        URL url = new URL(URL + "check/" + platformUUID + "/" + licenseKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        if (con.getResponseCode() != 200) return null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return GSON.fromJson(content.toString(), LicenseData.class);
    }

}
