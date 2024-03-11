package co.kr.compig.common.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsType implements BaseEnumCode<String> {
  SMS("S", "단문문자(SMS)"),
  LMS("L", "장문문자(LMS)"),
  MMS("M", "사진문자(MMS)"),
  ATA("A", "알림톡(ATA)"),
  FTA("F", "친구톡(FTA)"),
  ;

  private final String code;
  private final String desc;
}
