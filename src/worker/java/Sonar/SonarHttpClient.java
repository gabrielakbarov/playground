package Sonar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class SonarHttpClient {

    public JSONObject get(Map<String, String> params) {

        String token = Config.SQ_TOKEN + ":";
        String fullUrl = RequestBuilder.buildUrl(params);

        HttpURLConnection connection = null;

        try {
            URL url = new URI(fullUrl).toURL();
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            String encodedAuth = Base64.getEncoder()
                    .encodeToString(token.getBytes(StandardCharsets.UTF_8));

            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP Error: " + connection.getResponseCode());
            }

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(
                                 connection.getInputStream(),
                                 StandardCharsets.UTF_8))) {

                StringBuilder responseBody = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }

                return new JSONObject(responseBody.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Sonar request failed", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}