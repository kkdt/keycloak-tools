package kkdt.keycloak.desktop.controller;

import kkdt.keycloak.desktop.UserInfo;
import kkdt.keycloak.desktop.security.AuthenticationEvent;
import kkdt.keycloak.desktop.security.KeycloakAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

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
        if(event.getAuthentication() instanceof OAuth2AuthenticationToken authentication) {
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
                        return new UserInfo((OidcUserAuthority)oidc);
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
        switch(actionEvent.getActionCommand()) {
            case "Refresh":
                logger.info("Logging into keycloak and refreshing token, User: {}, {}",
                    currentUserInfo,
                    keycloakAccessTokenService);
                break;
        }
    }

    public void setAuthenticatedUser(Consumer<UserInfo> authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    private void logAuthority(GrantedAuthority authority) {
        logger.info("Authority {}: {}", authority.getClass().getName(), authority);
    }
}
