package co.edu.escuelaing.reflexionlab.http;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpRequestParserTest {

    @Test
    void shouldParsePathAndQueryParams() throws Exception {
        String rawRequest = "GET /greeting?name=Nicole&lang=es HTTP/1.1\r\n"
                + "Host: localhost\r\n"
                + "Connection: close\r\n\r\n";

        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest request = parser.parse(new BufferedReader(new StringReader(rawRequest)));

        assertEquals("GET", request.method());
        assertEquals("/greeting", request.path());
        assertEquals("Nicole", request.queryParams().get("name"));
        assertEquals("es", request.queryParams().get("lang"));
    }
}
