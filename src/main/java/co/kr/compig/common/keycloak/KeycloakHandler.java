package co.kr.compig.common.keycloak;

import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.exception.KeyCloakRequestException;
import co.kr.compig.domain.member.MemberGroup;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Keycloak ADMIN API Handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakHandler {

  private final KeycloakProperties keycloakProperties;
  @Getter
  private final PasswordEncoder passwordEncoder;

  private Keycloak keycloak;

  @PostConstruct
  public void initialize() {
    log.info("### Initializing Keycloak");
    keycloak = KeycloakBuilder.builder()
        .serverUrl(keycloakProperties.getServerUrl())
        .grantType(OAuth2Constants.PASSWORD)
        .realm(keycloakProperties.getRealm())
        .clientId(keycloakProperties.getClientId())
        .username(keycloakProperties.getUsername())
        .password(keycloakProperties.getPassword())
//        .resteasyClient(
//            new ResteasyClientBuilder()
//                .connectionPoolSize(keycloakProperties.getPoolSize()).build()
//        )
        .build();

    KeycloakHolder.set(this);

  }

  private RealmResource getRealmResource(Keycloak keycloak) {
    List<RealmRepresentation> realms = keycloak.realms().findAll();
    for (RealmRepresentation realm : realms) {
      if (realm.getRealm().equals(keycloakProperties.getRealm())) {
        return keycloak.realm(keycloakProperties.getRealm());
      }
    }
    return null;
  }

  public RealmResource getRealmResource() {
    return this.getRealmResource(this.keycloak);
  }

  /**
   *
   */
  public UsersResource getUsers() {
    log.debug("### Get users");
    return getRealmResource().users();
  }

  public Optional<UserRepresentation> getUser(String username) {
    log.debug("### Get user - {}", username);
    List<UserRepresentation> userRepresentationList = getRealmResource().users().search(username);
    if (userRepresentationList.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(userRepresentationList.get(0));
  }

  /**
   * Keycloak 사용자 생성
   */
  public UserRepresentation createUser(UserRepresentation userRepresentation)
      throws KeyCloakRequestException {
    Response response = getUsers().create(userRepresentation);
    int status = response.getStatus();
    if (status != HttpStatus.CREATED.value()) {
      if (status == HttpStatus.CONFLICT.value()) {
        //키클락 계정 충돌시
        throw new BizException("이미 가입한 이메일 입니다. 다른 이메일 주소를 사용해주세요.");
      }

      String reasonPhrase = ((ClientResponse) response).getReasonPhrase();
      log.error("Http status : {}, reason : {}", status, reasonPhrase);
      throw new KeyCloakRequestException("인증서버 등록중 에러가 발생 하였습니다.["
          + status + " - " + reasonPhrase + "]"
      );
    }
    return getUser(userRepresentation.getUsername())
        .orElseThrow(KeyCloakRequestException::new);
  }

  public void usersJoinGroups(String id, Set<MemberGroup> groups) {
    UserResource userResource = getUsers().get(id);
    groups.forEach(group -> userResource.joinGroup(group.getGroupKey()));

    List<GroupRepresentation> groupRepresentations = userResource.groups();
    for (MemberGroup memberGroup : groups) {
      Optional<GroupRepresentation> optionalGroupRepresentation = groupRepresentations.stream()
          .filter(
              groupRepresentation -> groupRepresentation.getId().equals(memberGroup.getGroupKey()))
          .findFirst();

      if (optionalGroupRepresentation.isEmpty()) {
        throw new KeyCloakRequestException("Not found Group : " + memberGroup.getGroupKey());
      }

      GroupRepresentation groupRepresentation = optionalGroupRepresentation.get();
      String groupNm = groupRepresentation.getName();
      String groupPath = groupRepresentation.getPath();
      memberGroup.updateGroupInfo(groupNm, groupPath);
    }
  }
}
