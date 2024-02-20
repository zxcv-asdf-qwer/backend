package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberRegisterType implements BaseEnumCode<String> {
  GENERAL("GENERAL", "member.register.type.general"), // 일반 회원가입
  KAKAO("KAKAO", "member.register.type.kakao"), // 카카오 회원가입
  NAVER("NAVER", "member.register.type.naver"), // 네이버 회원가입
  APPLE("APPLE", "member.register.type.apple"), // 애플 회원가입
  GOOGLE("GOOGLE", "member.register.type.google"), // 구글 회원가입
  ;

  private final String code;
  private final String desc;

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDesc() {
    return desc;
  }

}