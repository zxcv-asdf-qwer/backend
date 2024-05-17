package co.kr.compig.api.presentation.check;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.check.request.CheckNameRequest;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.notify.NotifyMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import niceid.namecheck.RNCheck;

@Slf4j
@Controller
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {

	@Value("${api.check.site-code.inner}")
	private String NAME_CHECK_SITE_CODE_INNER;
	@Value("${api.check.site-password.inner}")
	private String NAME_CHECK_SITE_PASSWORD_INNER;

	@Value("${api.check.site-code.outer}")
	private String NAME_CHECK_SITE_CODE_OUTER;
	@Value("${api.check.site-password.outer}")
	private String NAME_CHECK_SITE_PASSWORD_OUTER;

	private final NotifyMessage notifyMessage;

	private final MemberService memberService;

	@GetMapping(value = "/name")
	public ResponseEntity<?> checkName(
		@ModelAttribute @Valid CheckNameRequest checkNameRequest
	) {
		String sSiteCode = "";
		String sSitePw = "";

		// 처리결과코드 초기화
		int iRtn = 1;

		// 인증결과코드 초기화
		String Rtn = "";

		// 결과메세지 초기화
		String sRtnMsg = "";

		// 모듈객체 생성
		RNCheck ncClient = new RNCheck();

		// 입력 페이지에서 전달된 입력값 취득
		String sJumin1 = checkNameRequest.getJumin1();
		String sJumin2 = checkNameRequest.getJumin2();
		String sJumin = sJumin1 + sJumin2;
		String sName = checkNameRequest.getName();

		if (sJumin2.charAt(0) == '0' || sJumin2.charAt(0) == '1' ||
			sJumin2.charAt(0) == '2' || sJumin2.charAt(0) == '3' || sJumin2.charAt(0) == '4') {
			sSiteCode = NAME_CHECK_SITE_CODE_INNER;
			sSitePw = NAME_CHECK_SITE_PASSWORD_INNER;
		} else {
			sSiteCode = NAME_CHECK_SITE_CODE_OUTER;
			sSitePw = NAME_CHECK_SITE_PASSWORD_OUTER;
		}
		// 입력값 확인
		if (sJumin != null && sName != null) {
			iRtn = ncClient.fnRequest(sSiteCode, sSitePw, sJumin, sName);
		} else {
			sRtnMsg = "입력값 미전달 오류: 성명이나 주민번호가 전달되지 않았습니다.";
		}

		// 처리결과코드 확인
		if (iRtn == 0) {
			// 인증결과코드 추출
			Rtn = ncClient.getReturnCode();
		} else if (iRtn == -1) {
			sRtnMsg = "시스템 오류 :<br> 귀사 서버 환경에 맞는 모듈을 이용해주십시오.<br>오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
		} else if (iRtn == -2 || iRtn == -3) {
			sRtnMsg = "암호화 처리 오류 :<br> 귀사 서버 환경에 맞는 모듈을 이용해주십시오.<br>오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
		} else if (iRtn == -4 || iRtn == -5 || iRtn == -6) {
			sRtnMsg = "복호화 처리 오류 :<br> 귀사 서버 환경에 맞는 모듈을 이용해주십시오.<br>오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
		} else if (iRtn == -7 || iRtn == -8) {
			sRtnMsg = "암복호화 버전 오류:<br> 귀사 서버 환경에 맞는 모듈을 이용해주십시오.<br>오류가 지속되는 경우 iRtn 값, 서버 환경정보, 사이트코드를 기재해 문의주시기 바랍니다.";
		} else if (iRtn == -18) {
			sRtnMsg = "통신오류: 당사 서비스 IP를 방화벽에 등록해주십시오.<br>IP:203.234.219.72<br>port:81~85(총 5개 모두 등록)";
		} else {
			sRtnMsg = "기타오류: iRtn 값 확인 후 NICE평가정보 전산 담당자에게 문의해주시기 바랍니다.";
		}

		// 실명인증 결과코드 확인
		if (Rtn.equals("1")) {
			sRtnMsg = "인증성공";
			memberService.checkNameUpdate(checkNameRequest.getMemberId(), sJumin1, sJumin2);
		} else if (Rtn.equals("2")) {
			sRtnMsg = "성명불일치 오류: 주민번호와 성명이 일치하지 않습니다.<br>www.niceid.co.kr 에서 실명정보를 재등록하시거나 NICE 고객센터(1600-1522)로 문의해주십시오.";
		} else if (Rtn.equals("3")) {
			sRtnMsg = "자료없음 오류: 주민번호가 조회되지 않습니다.<br>www.niceid.co.kr 에서 실명정보를 등록하시거나  NICE 고객센터(1600-1522)로 문의해주십시오.";
		} else if (Rtn.equals("5")) {
			sRtnMsg = "주민번호 체크썸 오류: 주민번호 생성규칙에 맞지 않는 주민번호입니다.";
		} else if (Rtn.equals("9")) {
			sRtnMsg = "입력정보 오류: 입력정보가 누락되었거나 정상이 아닙니다."
				+ "<br>입력된 사이트코드, 패스워드, 주민번호, 성명 정보를 확인해주시기 바랍니다."
				+ "<br>일부 고객만 발생하는 경우 부정사용으로 인한 차단 오류입니다. 차단 처리는 일정 시간이 지나면 자동으로 해제됩니다.";
		} else if (Rtn.equals("10")) {
			sRtnMsg = "사이트 코드 오류: 사이트코드를 대문자로 입력해주십시오."
				+ "<br>사이트코드가 정상인 경우 내/외국인 설정 관련 오류입니다. (예:내국인 인증 계약 후 외국인 인증)"
				+ "<br>내/외국인 설정에 맞게 이용한 경우 NICE 계약/관리 담당자에게 정확한 설정 상태를 문의해주십시오.";
		} else if (Rtn.equals("11")) {
			sRtnMsg = "정지된 사이트 오류: 서비스 계약이 정지된 사이트입니다. NICE 계약/관리 담당자에게 문의해주십시오.";
		} else if (Rtn.equals("12")) {
			sRtnMsg = "패스워드 불일치 오류: 사이트 패스워드가 일치하지 않습니다. NICE 전산 담당자에게 문의해주십시오.";
		} else if (Rtn.equals("21")) {
			sRtnMsg = "입력정보 형식 오류: 입력정보의 자릿수를 확인해주십시오. (주민번호:13자리, 패스워드: 8자리)";
		} else if (Rtn.equals("31") || Rtn.equals("32") || Rtn.equals("34") || Rtn.equals("44")) {
			sRtnMsg = "통신오류: 당사 서비스 IP를 방화벽에 등록해주십시오.<br>IP:203.234.219.72<br>port:81~85(총 5개 모두 등록)";
		} else if (Rtn.equals("50")) {
			sRtnMsg = "명의도용차단 오류: 명의도용차단 서비스 이용 중인 주민번호입니다."
				+ "<br>www.credit.co.kr에서 명의도용차단 서비스 해제 후 재시도 하시거나 NICE고객센터(1600-1522)로 문의해주십시오.";
		} else if (Rtn.equals("60") || Rtn.equals("61") || Rtn.equals("62") || Rtn.equals("63")) {
			sRtnMsg = "네트워크 장애: 당사 서비스 IP와의 연결상태를 확인해주십시오.<br>IP:203.234.219.72<br>port:81~85(총 5개)";
		} else {
			sRtnMsg = "기타 오류: 리턴코드 문서에 기재된 내용을 확인해주십시오."
				+ "<br>코드가 문서에 기재되어 있지 않은 경우 NICE 전산담당자에게 문의해주십시오.";
		}

		StringBuilder stringBuilder = new StringBuilder();
		if (!Rtn.equals("1")) {
			try {
				stringBuilder.append(
						"##################################### 실명 인증 오류 #####################################")
					.append("\n");
				stringBuilder.append(String.format("sRtnMsg : %s", sRtnMsg)).append("\n");
				stringBuilder.append(
					"##################################### 실명 인증 오류 #####################################");
				throw new BizException(stringBuilder.toString());
			} catch (BizException e) {
				log.error(stringBuilder.toString());
				notifyMessage.sendErrorMessage(e, stringBuilder);
			}
			return ResponseEntity.badRequest().body(Map.of("msg", sRtnMsg));
		}
		return ResponseEntity.ok(Map.of("msg", "인증성공"));
	}
}
