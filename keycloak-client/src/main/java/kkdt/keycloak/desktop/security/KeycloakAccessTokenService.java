package kkdt.keycloak.desktop.security;

import kkdt.keycloak.desktop.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class KeycloakAccessTokenService {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakAccessTokenService.class);

    private final String realm;
    private String keycloakUrl;
    private String clientId;
    private String clientSecret;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public KeycloakAccessTokenService(OAuth2AuthorizedClientManager authorizedClientManager,
        OAuth2AuthorizedClientService authorizedClientService, String realm, String keycloakUrl, String clientId, String clientSecret)
    {
        this.authorizedClientManager = authorizedClientManager;
        this.authorizedClientService = authorizedClientService;
        this.realm = realm;
        this.keycloakUrl = keycloakUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Uses 'client_credentials' for obtaining the access token.
     *
     * @return
     */
    public String getClientCredentials() {
        WebClient webClient = WebClient.builder()
            .baseUrl(keycloakUrl)
            .build();
        return webClient.post()
            .uri("/protocol/openid-connect/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("scope", "openid"))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String getUserCredentials(UserInfo userInfo, char[] password) {
        WebClient webClient = WebClient.builder()
            .baseUrl(keycloakUrl)
            .build();
        return webClient.post()
            .uri("/protocol/openid-connect/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("username", userInfo.getUsername())
                .with("password", new String(password))
                .with("scope", "openid"))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
