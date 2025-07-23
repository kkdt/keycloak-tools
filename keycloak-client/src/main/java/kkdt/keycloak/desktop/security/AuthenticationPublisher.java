package kkdt.keycloak.desktop.security;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;

/**
 * Publish the {@link Authentication} to the entire application.
 */
public class AuthenticationPublisher implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void publishAuthentication(String source, Authentication authentication, String url) {
        AuthenticationEvent event = new AuthenticationEvent(source, authentication, url);
        applicationContext.publishEvent(event);
    }
}
