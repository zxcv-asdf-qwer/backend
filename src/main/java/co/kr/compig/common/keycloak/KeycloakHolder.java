package co.kr.compig.common.keycloak;

public class KeycloakHolder {
    private static KeycloakHandler keycloakHandler;

    public static void set(KeycloakHandler keycloak) {
        keycloakHandler = keycloak;
    }

    public static KeycloakHandler get() {
        return keycloakHandler;
    }

}
