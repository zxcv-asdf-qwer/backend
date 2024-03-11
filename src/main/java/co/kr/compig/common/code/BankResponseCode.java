package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankResponseCode implements BaseEnumCode<String>{
  NORMAL("0000", "정상처리", UseYn.Y),
  NOT_DATA_ERROR("DB01", "해당 데이터가 존재하지 않음", UseYn.Y),
  REAL_NAME_CHECK_DB_ERROR("DB02", "실명조회 DB 에러", UseYn.Y),
  ID_BUSINESS_NUMBER_ERROR("D100", "ID에 할당된 사업자번호 오류", UseYn.Y),
  JUMIN_NUMBER_ERROR("D200", "주민번호 오류", UseYn.Y),
  BUSINESS_NUMBER_ERROR("D300", "사업자번호 오류", UseYn.Y),
  ACCOUNT_CLASSIFICATION_ERROR("D400", "계좌구분 오류", UseYn.Y),
  SERVICE_CLASSIFICATION_ERROR("D500", "서비스 구분 오류", UseYn.Y),
  LG_DACON_KEY_ERROR("D600", "LG데이콤 Key 오류", UseYn.Y),
  TRANSACTION_DATE_ERROR("D700", "거래일자오류", UseYn.Y),
  TRANSACTION_TIME_ERROR("D800", "거래시간오류", UseYn.Y),
  INQUIRY_BANK_CODE_ERROR("D900", "조회은행코드오류", UseYn.Y),
  INQUIRY_DATE_BIRTH_ERROR("D101", "조회 생년월일 오류", UseYn.Y),
  INQUIRY_BUSINESS_NUMBER_ERROR("D102", "조회 사업자번호 오류", UseYn.Y),
  INQUIRY_ACCOUNT_NUMBER_ERROR("D103", "조회 계좌번호 오류", UseYn.Y),
  FLAG_ERROR("D104", "Flag오류", UseYn.Y),
  CLASSIFICATION_ERROR("D105", "구분 오류", UseYn.Y),
  TIMEOUT("TIME", "TIMEOUT(응답지연)", UseYn.Y),
  SYSTEM_ERROR("DSYS", "시스템 장애", UseYn.Y),
  EXCEEDED_NUMBER_CONCURRENT_USERS("OVER", "동시 접속자수 초과", UseYn.Y),
  BANK_SERVICE_ERROR("D888", "당행서비스가 불가능함", UseYn.Y),
  NOT_SERVICE_HOURS("D999", "서비스 시간 아님", UseYn.Y),
  ACCOUNT_HOLDER_NAME_MISMATCH("B004", "예금주명-불일치", UseYn.Y),
  UNREGISTERED_CODE("B005", "미등록코드", UseYn.Y),
  BANK_SYSTEM_ERROR("B101", "타행(공동망) or 은행시스템 오류", UseYn.Y),
  ACCOUNT_ERROR("B102", "계좌오류", UseYn.Y),
  PRIVATE_NUMBER_MISMATCH("B103", "생년월일 or 사업자번호 불일치", UseYn.Y),
  ACCOUNT_HOLDER_NAME_MISMATCH2("B104", "예금주명-불일치", UseYn.Y),
  BANK_OTHER_ERRORS("B199", "은행 기타 오류", UseYn.Y),
  CONNECTION_FAIL("C001", "Connection Fail", UseYn.Y),
  DATA_WRITE_FAIL("C002", "Data Write Fail", UseYn.Y),
  DATA_READ_FAIL("C003", "Data Read Fail", UseYn.Y),
  ACCOUNT_HOLDER_NAME_ERROR("S606", "계좌소유주명 오류", UseYn.Y),
  INNER_ERROR("E999", "내부 오류", UseYn.Y),
  ;
  private final String code;
  private final String desc;
  private final UseYn useYn;
}
