package kkdt.keycloak.desktop.config;

import kkdt.keycloak.desktop.UIFrame;
import kkdt.keycloak.desktop.UILoginPanel;
import kkdt.keycloak.desktop.controller.ApiHandler;
import kkdt.keycloak.desktop.controller.LoginController;
import kkdt.keycloak.desktop.security.KeycloakAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import javax.swing.JFrame;

@Configuration
public class GUIConfiguration {

    @Value("${app.title}")
    private String title;

    @Value("${app.realm}")
    private String realm;

    @Value("${app.keycloak.issuer}")
    private String keyloakUrl;

    @Value("${app.keycloak.client-id}")
    private String clientId;

    @Value("${app.keycloak.client-secret}")
    private String clientSecret;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    public ApiHandler apiHandler(@Value("${app.host}") String host, @Value("${server.port}") int port, @Value("${server.ssl.enabled}") boolean ssl) {
        return new ApiHandler(host, port, ssl);
    }

    @Bean
    public UILoginPanel loginPanel(@Autowired LoginController loginController) {
        return new UILoginPanel()
            .withLoginController(loginController);
    }

    @Bean
    public KeycloakAccessTokenService keycloakAccessTokenService(@Autowired OAuth2AuthorizedClientManager authorizedClientManager,
        @Autowired OAuth2AuthorizedClientService authorizedClientService)
    {
        return new KeycloakAccessTokenService(authorizedClientManager, authorizedClientService,
            realm, keyloakUrl, clientId, clientSecret);
    }

    @Bean
    public LoginController loginController(@Autowired KeycloakAccessTokenService keycloakAccessTokenService,
        @Autowired ApiHandler apiHandler)
    {
        return new LoginController(keycloakAccessTokenService, apiHandler);
    }

    @Bean
    public UIFrame mainWindow(@Autowired UILoginPanel loginPanel) {
        UIFrame frame = new UIFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContents(loginPanel);
        frame.setResizable(false);
        return frame;
    }

}
