package co.kr.compig.api.account.dto;

import co.kr.compig.common.code.BankCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCheckRequest {
  private String service; // 서비스 구분
  private String svcGbn;  // 업무 구분
  private String strGbn;  // 계좌 구분
  private BankCode strBankCode; // 은행코드 3자리
  private String strAccountNo;  // 계좌번호
  private String strNm; // 예금주명
  private String strResId;  // 생년월일/사업자번호
  private String strOrderNo;  // 주문번호
  private String inq_rsn; // 조회사유
}
