package kkdt.keycloak.desktop.config;

import kkdt.keycloak.desktop.UIFrame;
import kkdt.keycloak.desktop.UILoginPanel;
import kkdt.keycloak.desktop.controller.LoginController;
import kkdt.keycloak.desktop.security.KeycloakAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.JFrame;

@Configuration
public class GUIConfiguration {

    @Value("${app.title}")
    private String title;

    @Value("${app.realm}")
    private String realm;

    @Bean
    public UILoginPanel loginPanel(@Autowired LoginController loginController) {
        return new UILoginPanel()
            .withLoginController(loginController);
    }

    @Bean
    public KeycloakAccessTokenService keycloakAccessTokenService(@Autowired WebClient webClient) {
        return new KeycloakAccessTokenService(webClient, realm);
    }

    @Bean
    public LoginController loginController(@Autowired KeycloakAccessTokenService keycloakAccessTokenService) {
        return new LoginController(keycloakAccessTokenService);
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
