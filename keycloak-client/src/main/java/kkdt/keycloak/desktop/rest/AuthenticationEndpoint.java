package kkdt.keycloak.desktop.rest;

import kkdt.keycloak.desktop.security.AuthenticationPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AuthenticationEndpoint {

    @Autowired
    private AuthenticationPublisher authenticationPublisher;

    @RequestMapping(value = "/authentication", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Authentication> authentication() {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authenticationPublisher.publishAuthentication(AuthenticationEndpoint.class.getSimpleName(), authentication, url);
        return ResponseEntity.ok(authentication);
    }
}
