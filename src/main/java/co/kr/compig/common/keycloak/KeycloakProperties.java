package co.kr.compig.common.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("auth.build-info")
public class KeycloakProperties {

    private String serverUrl;
    private String realm;
    private String clientId;
    private String username;
    private String password;
    private int poolSize;

}
