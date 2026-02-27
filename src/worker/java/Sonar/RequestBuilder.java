package Sonar;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestBuilder {
    public static String buildUrl(Map<String, String> params){
        String fullUrl = Config.SQ_BASE_URL + Config.SQ_SI_ENDPOINT;

        String query = RequestBuilder.buildString(params);
        fullUrl += "?" + query;

        return fullUrl;
    }

    private static String buildString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!result.isEmpty()) {
                result.append("&");
            }
            result.append(encode(entry.getKey()))
                    .append("=")
                    .append(encode(entry.getValue()));
        }

        return result.toString();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
