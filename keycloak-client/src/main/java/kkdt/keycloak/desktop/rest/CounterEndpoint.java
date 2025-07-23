package kkdt.keycloak.desktop.rest;

import kkdt.keycloak.desktop.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CounterEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(CounterEndpoint.class);
    private static final AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping(value = "/counter", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Counter> counter() {
        logRequestUser();
        Counter results = new Counter()
            .with(c -> c.value = counter.incrementAndGet());
        return ResponseEntity.ok(results);
    }

    private void logRequestUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        logger.info("Security Context: {}, Authentication: {}, User info: {}",
            securityContext.getClass().getName(),
            authentication.getClass().getName(),
            user.getName());
    }
}
