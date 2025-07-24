# keycloak

## 1 Quick Start

> Navigate to http://localhost:8080 on a local browser after starting the pod

**Start the Pod**

```
podman kube play pod.yml
```

**Tail Logs**

```
podman pod logs -f keycloak
```

**Delete Pod and Resources**

```
podman kube play pod.yml --down
```

## 2 Initial Keycloak Configurations

## 2.1 Create Realm

Create realm `demo`

## 2.2 Create User

- Create user `guest`
- Disabled "Temporary" and set permanent password

## 2.3 Create Client Application ID

- Create client `apps` - Client ID and Name
- Enable "Client Authentication" and check all "Authentication Flow"
- Add "Valid redirect URIs" to `*` (development only)
- Add Web Origins: `/*`
- Note "Client Secret" from Credentials tab

[//]: Links

[podman-keycloak]: https://www.keycloak.org/getting-started/getting-started-podman