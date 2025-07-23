package kkdt.keycloak.desktop.security;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class KeycloakAccessTokenService {
    private final WebClient webClient;
    private final String realm;
    private String keycloakUrl;
    private String clientId;
    private String clientSecret;

    public KeycloakAccessTokenService(WebClient webClient, String realm) {
        this.webClient = webClient;
        this.realm = realm;
    }

    public String getAccessToken(String username, char[] password) {
        String tokenEndpoint = String.format("/realms/%s/protocol/openid-connect/token", realm);
        WebClient webClient = WebClient.builder().baseUrl(keycloakUrl).build();


        return webClient.post()
            .uri(tokenEndpoint)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData("grant_type", "password")
                .with("username", username)
                .with("password", new String(password))
                .with("scope", "openid"))
            .retrieve()
            .bodyToMono(String.class) // You'll need to parse the JSON response for the access_token
            .block();
    }
}
