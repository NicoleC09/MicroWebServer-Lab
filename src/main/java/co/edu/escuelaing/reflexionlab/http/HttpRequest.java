package co.edu.escuelaing.reflexionlab.http;

import java.util.Collections;
import java.util.Map;

public record HttpRequest(String method, String path, Map<String, String> queryParams) {

    public HttpRequest {
        queryParams = Collections.unmodifiableMap(queryParams);
    }
}
