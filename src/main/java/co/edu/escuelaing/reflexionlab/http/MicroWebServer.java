package co.edu.escuelaing.reflexionlab.http;

import co.edu.escuelaing.reflexionlab.framework.SimpleApplicationContext;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class MicroWebServer {

    private final int port;
    private final SimpleApplicationContext context;
    private final HttpRequestParser requestParser = new HttpRequestParser();

    public MicroWebServer(int port, SimpleApplicationContext context) {
        this.port = port;
        this.context = context;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("MicroWebServer listening on http://localhost:" + port);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (Exception e) {
                    System.err.println("Error handling request: " + e.getMessage());
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        OutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());

        HttpRequest request = requestParser.parse(reader);
        if (request == null) {
            return;
        }

        if (!"GET".equals(request.method())) {
            sendTextResponse(outputStream, "405 Method Not Allowed", "text/plain", "Only GET is supported");
            return;
        }

        Optional<String> dynamicResponse = context.executeGetRoute(request.path(), request.queryParams());
        if (dynamicResponse.isPresent()) {
            sendTextResponse(outputStream, "200 OK", "text/plain", dynamicResponse.get());
            return;
        }

        serveStaticContent(outputStream, request.path());
    }

    private void serveStaticContent(OutputStream outputStream, String path) throws IOException {
        String normalizedPath = "/".equals(path) ? "/index.html" : path;
        String resourcePath = "public" + normalizedPath;

        try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (resourceStream == null) {
                sendTextResponse(outputStream, "404 Not Found", "text/plain", "Resource not found");
                return;
            }

            byte[] content = resourceStream.readAllBytes();
            String contentType = resolveContentType(normalizedPath);
            sendBinaryResponse(outputStream, "200 OK", contentType, content);
        }
    }

    private String resolveContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        }
        if (path.endsWith(".png")) {
            return "image/png";
        }
        return "application/octet-stream";
    }

    private void sendTextResponse(OutputStream outputStream, String status, String contentType, String body)
            throws IOException {
        sendBinaryResponse(outputStream, status, contentType, body.getBytes(StandardCharsets.UTF_8));
    }

    private void sendBinaryResponse(OutputStream outputStream, String status, String contentType, byte[] body)
            throws IOException {
        String headers = "HTTP/1.1 " + status + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n\r\n";

        outputStream.write(headers.getBytes(StandardCharsets.UTF_8));
        outputStream.write(body);
        outputStream.flush();
    }
}
