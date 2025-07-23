package kkdt.keycloak.desktop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kkdt.keycloak.desktop.UserInfo;
import kkdt.keycloak.desktop.security.AuthenticationEvent;
import kkdt.keycloak.desktop.security.KeycloakAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.function.Consumer;

public class LoginController implements ActionListener, ApplicationListener<AuthenticationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private Consumer<UserInfo> authenticatedUser;
    private KeycloakAccessTokenService keycloakAccessTokenService;
    private UserInfo currentUserInfo;

    public LoginController(KeycloakAccessTokenService keycloakAccessTokenService) {
        this.keycloakAccessTokenService = keycloakAccessTokenService;
    }

    @Override
    public void onApplicationEvent(AuthenticationEvent event) {
        Object source = event.getSource();
        if(event.getAuthentication() instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) event.getAuthentication();
            Object credentials = authentication.getCredentials();
            Collection<GrantedAuthority> authorities = authentication.getAuthorities();
            logger.info("Received OAuth2AuthenticationToken {}, URL: {}, Authorities: {}, Credentials: {}",
                authentication,
                event.getUrl(),
                authorities.size(),
                credentials);
            authorities.forEach(this::logAuthority);
            if(authenticatedUser != null) {
                authorities.stream().filter(a -> a instanceof OidcUserAuthority)
                    .findFirst()
                    .map(oidc -> {
                        UserInfo info = new UserInfo(authentication);
                        info.setAuthority((OidcUserAuthority)oidc);
                        info.setSource(source.toString());
                        return info;
                    })
                    .ifPresent(user -> {
                        logger.info("User authenticated: {}", user.getUserInfo());
                        this.currentUserInfo = user;
                        authenticatedUser.accept(user);
                    });
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            switch (actionEvent.getActionCommand()) {
                case "Client":
                    logger.info("Logging into keycloak and refreshing token, User: {}, Token: {}",
                        currentUserInfo,
                        keycloakAccessTokenService);
                    String clientCredentials = keycloakAccessTokenService.getClientCredentials();
                    logger.info("Client credentials: {}", clientCredentials);
                    displayResponse(clientCredentials, "Client Credentials");
                    break;
                case "User":
                    handleUserAction();
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Exception: " + e.getMessage(),
                "Error encountered",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setAuthenticatedUser(Consumer<UserInfo> authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    private void logAuthority(GrantedAuthority authority) {
        logger.info("Authority {}: {}", authority.getClass().getName(), authority);
    }

    private void handleUserAction() throws JsonProcessingException {
        if(currentUserInfo == null) {
            JOptionPane.showMessageDialog(null,
                "Please perform the initial login on the browser",
                "Unauthenticated",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField password = new JPasswordField();
        int confirm = JOptionPane.showConfirmDialog(
            null,
            password,
            String.format("Enter Password %s", currentUserInfo.getUsername()),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (confirm == JOptionPane.OK_OPTION) {
            char[] _input = password.getPassword();
            String userCredentials = keycloakAccessTokenService.getUserCredentials(currentUserInfo, _input);
            logger.info("User credentials: {}", userCredentials);
            displayResponse(userCredentials, "User Credentials");
        }
    }

    private void displayResponse(String response, String title) throws JsonProcessingException {
        JTextArea area = new JTextArea(25, 30);
        area.setLineWrap(false);
        area.setWrapStyleWord(false);

        JScrollPane scrollPane = new JScrollPane(area,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(objectMapper.readTree(response));

        area.setText(json);
        logger.info(String.format("JSON Response: \n    %s", json));

        JOptionPane.showConfirmDialog(
            null,
            scrollPane,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
    }
}
