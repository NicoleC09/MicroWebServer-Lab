package co.edu.escuelaing.reflexionlab.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public HttpRequest parse(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            return null;
        }

        String[] tokens = requestLine.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Invalid HTTP request line");
        }

        String method = tokens[0];
        String rawPath = tokens[1];

        // Consume headers to leave stream in a consistent state.
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            // No-op.
        }

        return new HttpRequest(method, extractPath(rawPath), extractQueryParams(rawPath));
    }

    private String extractPath(String rawPath) {
        int queryStart = rawPath.indexOf('?');
        return queryStart >= 0 ? rawPath.substring(0, queryStart) : rawPath;
    }

    private Map<String, String> extractQueryParams(String rawPath) {
        Map<String, String> queryParams = new HashMap<>();
        int queryStart = rawPath.indexOf('?');
        if (queryStart < 0 || queryStart == rawPath.length() - 1) {
            return queryParams;
        }

        String query = rawPath.substring(queryStart + 1);
        for (String pair : query.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            String[] keyValue = pair.split("=", 2);
            String key = decode(keyValue[0]);
            String value = keyValue.length > 1 ? decode(keyValue[1]) : "";
            queryParams.put(key, value);
        }
        return queryParams;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
