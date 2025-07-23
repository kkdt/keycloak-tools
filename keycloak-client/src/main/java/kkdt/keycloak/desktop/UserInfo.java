package kkdt.keycloak.desktop;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.net.URL;
import java.time.Instant;

public class UserInfo {
    private OidcUserAuthority authority;
    private OAuth2AuthenticationToken authentication;
    private String source;
    private char[] password;

    public UserInfo(OAuth2AuthenticationToken authentication) {
        this.authentication = authentication;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public OidcUserAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(OidcUserAuthority authority) {
        this.authority = authority;
    }

    public OAuth2AuthenticationToken getAuthentication() {
        return authentication;
    }

    public String getUsername() {
        String value = "";
        if (authority != null) {
            value = authority.getUserInfo().getPreferredUsername();
        }
        return value;
    }

    public String getEmail() {
        String value = "";
        if (authority != null) {
            value = authority.getUserInfo().getEmail();
        }
        return value;
    }

    public String getSubject() {
        String value = "";
        if (authority != null) {
            value = authority.getUserInfo().getSubject();
        }
        return value;
    }

    public String getName() {
        String value = "";
        if (authority != null) {
            value = authority.getUserInfo().getFullName();
        }
        return value;
    }

    public String getToken() {
        String value = "";
        if (authority != null) {
            value = authority.getIdToken().getTokenValue();
        }
        return value;
    }

    public Instant getIssuedAt() {
        Instant value = null;
        if (authority != null) {
            value = authority.getIdToken().getIssuedAt();
        }
        return value;
    }

    public Instant getExpiresAt() {
        Instant value = null;
        if (authority != null) {
            value = authority.getIdToken().getExpiresAt();
        }
        return value;
    }

    public URL getIssuer() {
        URL value = null;
        if (authority != null) {
            value = authority.getIdToken().getIssuer();
        }
        return value;
    }

    public String getAuthorizedParty() {
        String value = "";
        if (authority != null) {
            value = authority.getIdToken().getAuthorizedParty();
        }
        return value;
    }

    public OidcUserInfo getUserInfo() {
        OidcUserInfo info = null;
        if (authority != null) {
            info = authority.getUserInfo();
        }
        return info;
    }

    public OidcIdToken getIdToken() {
        OidcIdToken idToken = null;
        if(authority != null) {
            idToken = authority.getIdToken();
        }
        return idToken;
    }
}
