package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SmsType implements BaseEnumCode<String> {

  SMS("SMS", "국내 SMS 발송"),
  ISM("ISM", "국제 SMS 발송"),
  MMS("MMS", "이미지 발송"),
  KAT("KAT", "알림톡 발송"),
  KFT("KFT", "친구톡 텍스트 발송"),
  KFP("KFP", "친구톡 텍스트 발송");

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
