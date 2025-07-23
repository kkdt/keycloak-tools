# keycloak-tools

## Overview

1. Set up Keycloak for testing, see [README](keycloak/README.md)

2. Build and run the `keycloak-client` application, see [README](keycloak-client/README.md)

## Enable SSL

1. Create SSL Certificate / private keystore
    ```
    keytool -genkeypair -alias keycloak-client -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore keycloak-client.p12 -validity 3650 -storepass password
   
    Enter the distinguished name. Provide a single dot (.) to leave a sub-component empty or press ENTER to use the default value in braces.
    What is your first and last name?
    [Unknown]:  keycloak-client
    What is the name of your organizational unit?
    [Unknown]:  Development
    What is the name of your organization?
    [Unknown]:  Development
    What is the name of your City or Locality?
    [Unknown]:  Fairfax
    What is the name of your State or Province?
    [Unknown]:  VA
    What is the two-letter country code for this unit?
    [Unknown]:  US
    Is CN=keycloak-client, OU=Development, O=Development, L=Fairfax, ST=VA, C=US correct?
    [no]:  yes
    Generating 4,096 bit RSA key pair and self-signed certificate (SHA384withRSA) with a validity of 3,650 days
	  for: CN=keycloak-client, OU=Development, O=Development, L=Fairfax, ST=VA, C=US
    ```

2. Export self-signed certificate from the the previous step
    ```
    keytool -exportcert -alias keycloak-client -file keycloak-client.crt -keystore keycloak-client.p12
    ```
   
3. Import the certificate into the truststore
    ```
    keytool -importcert -alias keycloak-client -file keycloak-client.crt -keystore truststore.jks
    ```
   
4. When running the application, use the `--ssl` option to enable SSL

[//]: Links

[spring-ssl]: https://www.thomasvitale.com/https-spring-boot-ssl-certificate/