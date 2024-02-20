package co.kr.compig.common.util;

import co.kr.compig.common.code.RoleCode;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.security.CustomOauth2User;
import co.kr.compig.common.security.CustomOauth2UserAuthenticatedToken;
import co.kr.compig.common.security.converter.CustomJwtAuthenticationConverter;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class SecurityUtil {

  public final static String TEST_TOKEN = "testToken";

  /**
   * SecurityContextHolder에 userId, role에 대한 테스트 토큰 삽입
   */
  public static void setUser(String userId, String role) {
    Assert.notNull(userId, "userId is require");
    Assert.notNull(role, "role is require");

    if (role.split("ROLE_").length == 1) {
      role = "ROLE_" + role;
    }

    CustomOauth2UserAuthenticatedToken token =
        new CustomOauth2UserAuthenticatedToken(
            new Jwt(SecurityUtil.TEST_TOKEN, Instant.now(), Instant.MAX, Map.of("header", "header"),
                Map.of("claim", "claim")),
            Set.of(new SimpleGrantedAuthority(role)),
            CustomOauth2User.builder().userId(userId).build());

    SecurityContextHolder.getContext().setAuthentication(token);
  }

  public static void setISOToken(CustomOauth2UserAuthenticatedToken token) {
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  /**
   * See {@link CustomJwtAuthenticationConverter#getCustomOauth2User(Jwt)} 인증정보 없는 경우 null 리턴
   *
   * @return {@link CustomOauth2User#getUserId()}
   */
  public static String getUserId() {
    CustomOauth2User user = getCustomOauth2User();

    return user == null ? null : user.getUserId();
  }

  /**
   * See {@link CustomJwtAuthenticationConverter#getCustomOauth2User(Jwt)} 인증정보 없는 경우 null 리턴
   *
   * @return {@link CustomOauth2User#getCustCd()}
   */
  public static String getCustCd() {
    CustomOauth2User user = getCustomOauth2User();

    return user == null ? null : user.getCustCd();
  }

  /**
   * 인증정보 없으면 null 리턴
   *
   * @return {@link CustomOauth2User} 토큰 사용자 정보
   */
  public static CustomOauth2User getCustomOauth2User() {
    CustomOauth2UserAuthenticatedToken authenticatedToken = getAuthentication();

    return authenticatedToken == null ? null : authenticatedToken.getCredentials();
  }

  /**
   * 인증정보 없으면 null 리턴
   *
   * @return JWT 토큰
   */
  public static Jwt getToken() {
    CustomOauth2UserAuthenticatedToken authenticatedToken = getAuthentication();

    return authenticatedToken == null ? null : authenticatedToken.getToken();
  }

  /**
   * 인증정보 없으면 null 리턴
   *
   * @return JWT 토큰 value
   */
  public static String getTokenValue() {
    Jwt token = getToken();

    if (token != null && !token.getTokenValue().equalsIgnoreCase(TEST_TOKEN)) {
      return token.getTokenValue();
    } else {
      return null;
    }
  }

  /**
   * @return 권한 정보
   */
  public static Collection<GrantedAuthority> getRole() {
    CustomOauth2UserAuthenticatedToken authenticatedToken = getAuthentication();

    return authenticatedToken == null ? Collections.emptyList()
        : authenticatedToken.getAuthorities();
  }

  /**
   * @return {@link RoleCode#SYS_ADMIN} 권한 여부
   */
  public static boolean hasRoleAdmin() {
    return getRole().stream().anyMatch(a -> RoleCode.SYS_ADMIN.hasRole(a.getAuthority()));
  }

  /**
   * @return {@link RoleCode#INT_SYSTEM} 권한 여부
   */
  public static boolean hasRoleSystem() {
    return getRole().stream().anyMatch(a -> RoleCode.INT_SYSTEM.hasRole(a.getAuthority()));
  }

  /**
   * @return {@link RoleCode#PARTNER} 권한 여부
   */
  public static boolean hasRolePartner() {
    return getRole().stream().anyMatch(a -> RoleCode.PARTNER.hasRole(a.getAuthority()));
  }

  private static CustomOauth2UserAuthenticatedToken getAuthentication() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication instanceof CustomOauth2UserAuthenticatedToken) {
        return (CustomOauth2UserAuthenticatedToken) authentication;
      } else {
        throw new BizException("알 수 없는 인증 정보");
      }

      //return (CustomOauth2UserAuthenticatedToken) SecurityContextHolder.getContext().getAuthentication();
    } catch (Exception e) {
      return null;
    }
  }
}
