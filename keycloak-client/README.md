# keycloak-client

1. Build
2. Unzip and run the [keycloak-client.sh](src/dist/keycloak-client.sh) script
3. Open a browser to authenticate: `http://localhost:8081/authentication`
4. Confirm the Desktop client displays the authentication

## Login / Request Token

```
curl --location --request POST 'http://localhost:8080/realms/kkdt/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=apps' \
--data-urlencode 'client_secret=E57lksP4Xo9LfmFWXmHcVIKXF1eER0dK' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=guest' \
--data-urlencode 'password=password'
```

## Refresh Token

```
curl --location --request POST 'http://localhost:8080/realms/kkdt/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=apps' \
--data-urlencode 'client_secret=E57lksP4Xo9LfmFWXmHcVIKXF1eER0dK' \
--data-urlencode 'grant_type=refresh_token' \
--data-urlencode 'refresh_token=<...>'
```

[//]: Links

[keycloak-spring-boot]: https://developers.redhat.com/articles/2023/07/24/how-integrate-spring-boot-3-spring-security-and-keycloak
[webclient-example]: https://github.com/justin-tay/keycloak-spring-boot-example/blob/main/src/main/java/com/example/app/web/server/config/WebClientConfiguration.java
[authorized-client]: https://docs.spring.io/spring-security/reference/servlet/oauth2/client/authorized-clients.html
[curl-commands]: https://stackoverflow.com/questions/77652444/how-can-i-use-keycloak-login-inside-a-react-native-app-without-redirecting-the