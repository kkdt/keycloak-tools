package kkdt.keycloak.desktop.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

/**
 * An event that can be passed around the Spring <code>ApplicationContext</code>.
 */
public class AuthenticationEvent extends ApplicationEvent {
    private final Authentication authentication;
    private final String url;

    public AuthenticationEvent(Object source, Authentication authentication, String url) {
        super(source);
        this.authentication = authentication;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
