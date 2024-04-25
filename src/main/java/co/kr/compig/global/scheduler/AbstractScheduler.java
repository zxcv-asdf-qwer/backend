package co.kr.compig.global.scheduler;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import co.kr.compig.global.security.CustomOauth2User;
import co.kr.compig.global.security.CustomOauth2UserAuthenticatedToken;
import co.kr.compig.global.utils.SecurityUtil;

public abstract class AbstractScheduler {
  // crontab
  // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 년도(생략가능)
  // 요일에서 0과 7은 일요일이며, 1부터 월요일 6이 토요일

  protected void setupToken() {
    CustomOauth2UserAuthenticatedToken token =
        new CustomOauth2UserAuthenticatedToken(
            new Jwt(
                SecurityUtil.TEST_TOKEN, Instant.now(), Instant.MAX, Map.of("header", "header"), Map.of("claim", "claim")),
            Collections.emptyList(),
            CustomOauth2User.builder()
                .id("SCHEDULER")
                .build());

    SecurityContextHolder.getContext().setAuthentication(token);
  }
}
