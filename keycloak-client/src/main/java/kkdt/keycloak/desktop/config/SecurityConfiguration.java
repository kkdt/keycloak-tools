package kkdt.keycloak.desktop.config;

import kkdt.keycloak.desktop.security.AuthenticationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Value("${app.keycloak.client-id}")
    private String clientId;

    @Value("${app.keycloak.client-secret}")
    private String clientSecret;

    @Value("${app.keycloak.jwk}")
    private String url;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public AuthenticationPublisher authenticationPublisher() {
        return new AuthenticationPublisher();
    }

    /**
     * TODO - Experimenting and exposing {@link OAuth2AuthorizedClientManager} to see what it does.
     *
     * @param clientRegistrationRepository
     * @param authorizedClientRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
         OAuth2AuthorizedClientRepository authorizedClientRepository)
    {
        logger.info("Creating OAuth2AuthorizedClientManager, ClientRegistrationRepository: {}, OAuth2AuthorizedClientRepository: {}",
            clientRegistrationRepository,
            authorizedClientRepository);

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
            new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        logger.info("Creating OAuth2AuthorizedClientManager {}", authorizedClientManager);
        return authorizedClientManager;
    }

    /**
     * TODO - Experimenting and exposing {@link WebClient} to see what it does.
     *
     * @param authorizedClientManager
     * @return
     */
    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        logger.info("Configuring web client using {}", authorizedClientManager);

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
            .apply(oauth2Client.oauth2Configuration())
            .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .oauth2Client()
            .and()
            .oauth2Login()
            .tokenEndpoint()
            .and()
            .userInfoEndpoint();
        http.authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated() // All requests require authentication
        )
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt -> jwt.jwkSetUri(url))
        );
        return http.build();
    }
}
