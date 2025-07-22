# keycloak

## 1 Quick Start

> Navigate to http://localhost:8080 on a local browser after starting the pod

**Start the Pod**

```
podman kube play keycloak/pod.yml
```

**Tail Logs**

```
podman pod logs -f keycloak
```

**Delete Pod and Resources**

```
podman kube play keycloak/pod.yml --down
```

## 2 Initial Keycloak Configurations

## 2.1 Create Realm

## 2.2 Create User

## 2.3 Create Client Application ID

[//]: Links

[podman-keycloak]: https://www.keycloak.org/getting-started/getting-started-podman