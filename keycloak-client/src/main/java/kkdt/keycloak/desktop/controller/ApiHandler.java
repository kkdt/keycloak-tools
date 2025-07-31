package kkdt.keycloak.desktop.controller;

import kkdt.keycloak.desktop.security.ClientCredentials;
import kkdt.keycloak.desktop.security.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiHandler {
    private final String host;
    private final int port;
    private final boolean ssl;

    public ApiHandler(String host, int port, boolean ssl) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
    }

    // Attempt 1
    public <T> ResponseEntity<T> invoke(Class<T> expectedType, String endpoint, HttpMethod method,
        ClientCredentials clientCredentials)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s://%s:%s/%s", ssl ? "https" : "http", host, port, endpoint);
        HttpHeaders headers = clientCredentials != null ?
            createHeaders(clientCredentials.getToken_type(), clientCredentials.getId_token()) : new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(
            url,
            method,
            entity,
            expectedType
        );
        return response;
    }

    // Attempt 2
    public String invoke(String endpoint, UserCredentials userCredentials)
    {
        String baseUrl = String.format("%s://%s:%s", ssl ? "https" : "http", host, port);
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return webClient.get()
            .uri(endpoint)
            .header("Authorization", "Bearer", userCredentials.getAccess_token())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    // Attempt 3
    public String invoke(String endpoint, String accessToken) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("%s://%s:%s", ssl ? "https" : "http", host, port);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + endpoint))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("API call failed with status: " + response.statusCode() + " - " + response.body());
        }
    }

    private HttpHeaders createHeaders(String type, String token) {
        return new HttpHeaders() {{
            String authHeader = String.format("%s %s", type, token);
            set( "Authorization", authHeader );
        }};
    }
}
