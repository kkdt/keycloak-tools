package kkdt.keycloak.desktop;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.security.Security;

@SpringBootApplication
public class KeycloakDesktopClientApplication {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        SpringApplicationBuilder app = new SpringApplicationBuilder(KeycloakDesktopClientApplication.class)
            .bannerMode(Banner.Mode.OFF)
            .logStartupInfo(false)
            .headless(false);
        app.run(args);
    }
}
