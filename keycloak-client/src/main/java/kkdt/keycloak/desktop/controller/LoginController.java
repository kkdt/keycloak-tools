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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.FlowLayout;
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

            JOptionPane.showMessageDialog(null,
                String.format("Logged in as: %s", currentUserInfo.getUsername()),
                "Browser Authentication",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            switch (actionEvent.getActionCommand()) {
                case "Client ID":
                    logger.info("Logging into keycloak and refreshing token, User: {}, Token: {}",
                        currentUserInfo,
                        keycloakAccessTokenService);
                    String clientCredentials = keycloakAccessTokenService.getClientCredentials();
                    logger.info("Client credentials: {}", clientCredentials);
                    displayResponse(clientCredentials, "Client Credentials");
                    break;
                case "Password":
                    handleUserPasswordAction();
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

    private void handleUserPasswordAction() throws JsonProcessingException {
        JLabel label1 = new JLabel("Username");
        JLabel label2 = new JLabel("Password");
        JTextField input1 = new JTextField(20);
        JPasswordField password = new JPasswordField(20);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label1);
        panel.add(input1);
        panel.add(label2);
        panel.add(password);

        int confirm = JOptionPane.showConfirmDialog(
            null,
            panel,
            "User Authentication",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (confirm == JOptionPane.OK_OPTION) {
            char[] _input = password.getPassword();
            String userCredentials = keycloakAccessTokenService.getUserCredentials(input1.getText(), _input);
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

        JOptionPane.showMessageDialog(null,
            scrollPane,
            title,
            JOptionPane.INFORMATION_MESSAGE);
    }
}
