package co.kr.compig.api.presentation.pass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import NiceID.Check.CPClient;
import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.pass.response.PassResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.notify.NotifyMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "PASS", description = "PASS 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@Controller
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PassController {
	//TODO refactoring
	@Value("${api.pass.site-code}")
	private String NICE_PASS_SITE_CODE;
	@Value("${api.pass.site-password}")
	private String NICE_PASS_SITE_PW;
	@Value("${api.pass.redirect-url}")
	private String clientUrl;
	@Value("${server.url}")
	private String serverUrl;
	private final MemberService memberService;
	private final NotifyMessage notifyMessage;

	@Operation(summary = "url 만들기")
	@GetMapping
	public ResponseEntity<Response<?>> getAdminPassTestPage(HttpServletRequest request, HttpServletResponse response,
		ModelMap modelMap) {
		CPClient niceCheck = new CPClient();

		String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
		String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드

		String sRequestNumber = niceCheck.getRequestNO(sSiteCode);        // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로

		String sAuthType = "";        // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
		String customize = "";        // 없으면 기본 웹페이지 / Mobile : 모바일페이지

		String sReturnUrl = "";
		String sErrorUrl = "";

		sReturnUrl = clientUrl + "/admin/registration/partner"; // 성공시 이동될 URL
		sErrorUrl = serverUrl + "/pass/fail"; // 실패시 이동될 URL

		// 입력될 plain 데이타를 만든다.
		String sPlainData =
			"7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
				"8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
				"9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
				"7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
				"7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
				"9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;

		String sMessage = "";
		String sEncData = "";

		int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
		if (iReturn == 0) {
			sEncData = niceCheck.getCipherData();
		} else if (iReturn == -1) {
			sMessage = "암호화 시스템 에러입니다.";
		} else if (iReturn == -2) {
			sMessage = "암호화 처리오류입니다.";
		} else if (iReturn == -3) {
			sMessage = "암호화 데이터 오류입니다.";
		} else if (iReturn == -9) {
			sMessage = "입력 데이터 오류입니다.";
		} else {
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}

		request.getSession().setAttribute("REQ_SEQ", sRequestNumber);

		StringBuilder stringBuilder = new StringBuilder();
		if (!sMessage.isEmpty()) {
			try {
				stringBuilder.append(
					"##################################### PASS 인증 오류 #####################################");
				stringBuilder.append(String.format("sPlainData : %s,", sPlainData));
				stringBuilder.append(String.format("sMessage : %s,", sMessage));
				stringBuilder.append(
					"##################################### PASS 인증 오류 #####################################");
				throw new BizException(stringBuilder.toString());
			} catch (BizException e) {
				log.error(stringBuilder.toString());

				notifyMessage.sendErrorMessage(e);
			}
		}

		return ResponseEntity.ok().body(Response.builder().data(Map.of("passUrl", sEncData)).build());
	}

	@Operation(summary = "본인인증 성공", hidden = true)
	@RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> adminPassSuccess(HttpServletRequest request, HttpServletResponse response,
		ModelMap modelMap) {
		CPClient niceCheck = new CPClient();

		String sEncodeData = request.getParameter("EncodeData");
		String memberId = request.getParameter("memberId");

		String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
		String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드

		String sCipherTime = "";            // 복호화한 시간
		String sRequestNumber = "";            // 요청 번호
		String sResponseNumber = "";        // 인증 고유번호
		String sAuthType = "";                // 인증 수단
		String sMessage = "";
		String sPlainData = "";
		try {
			int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
			// 추출한 정보를 이용하여 redirect URL 생성
			if (iReturn == 0) {
				sPlainData = niceCheck.getPlainData();
				sCipherTime = niceCheck.getCipherDateTime();

				// 데이타를 추출합니다.
				HashMap mapresult = niceCheck.fnParse(sPlainData);
				sRequestNumber = (String)mapresult.get("REQ_SEQ");
				sResponseNumber = (String)mapresult.get("RES_SEQ");
				sAuthType = (String)mapresult.get("AUTH_TYPE");

				String session_sRequestNumber = (String)request.getSession().getAttribute("REQ_SEQ");
				if (!sRequestNumber.equals(session_sRequestNumber)) {
					throw new BizException("세션값 불일치 오류입니다.");
				}

				PassResponse passResponse = PassResponse.builder()
					.sRequestNumber(sRequestNumber)
					.sResponseNumber(sResponseNumber)
					.sAuthType(sAuthType)
					.sCipherTime(sCipherTime)
					.name((String)mapresult.get("NAME"))
					.birth((String)mapresult.get("BIRTHDATE"))
					.gender((String)mapresult.get("GENDER"))
					.nationalInfo((String)mapresult.get("NATIONAINFO"))
					.dupInfo((String)mapresult.get("DI"))
					.connInfo((String)mapresult.get("CI"))
					.phone((String)mapresult.get("MOBILE_NO"))
					.mobileCompany((String)mapresult.get("MOBILE_CO"))
					.build();
				if (memberId != null) {
					return ResponseEntity.ok(memberService.passUpdate(memberId, passResponse));
				}
				return ResponseEntity.ok(passResponse);
			} else if (iReturn == -1) {
				throw new BizException("복호화 시스템 오류입니다.");
			} else if (iReturn == -4) {
				throw new BizException("복호화 처리 오류입니다.");
			} else if (iReturn == -5) {
				throw new BizException("복호화 해쉬 오류입니다.");
			} else if (iReturn == -6) {
				throw new BizException("복호화 데이터 오류입니다.");
			} else if (iReturn == -9) {
				throw new BizException("입력 데이터 오류입니다.");
			} else if (iReturn == -12) {
				throw new BizException("사이트 패스워드 오류입니다.");
			} else {
				throw new BizException("알수 없는 에러 입니다. iReturn : " + iReturn);
			}

		} catch (BizException e) {
			String stringBuilder =
				"##################################### PASS 인증 오류 #####################################"
					+ String.format("sPlainData : %s,", sPlainData)
					+ String.format("sMessage : %s,", sMessage)
					+ "##################################### PASS 인증 오류 #####################################";
			log.error(stringBuilder);
			notifyMessage.sendErrorMessage(e, stringBuilder);
		}
		return ResponseEntity.badRequest().body("PASS 인증 오류");
	}

	@Operation(summary = "본인인증 실패", hidden = true)
	@RequestMapping(value = "/fail", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> adminPassFail(HttpServletRequest request, HttpServletResponse response,
		ModelMap modelMap) {
		CPClient niceCheck = new CPClient();

		String sEncodeData = request.getParameter("EncodeData");

		String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
		String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드

		String sCipherTime = "";            // 복호화한 시간
		String sRequestNumber = "";         // 요청 번호
		String sErrorCode = "";                // 인증 결과코드
		String sAuthType = "";              // 인증 수단
		String sMessage = "";
		String sPlainData = "";
		try {
			int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
			// 추출한 정보를 이용하여 redirect URL 생성
			if (iReturn == 0) {
				sPlainData = niceCheck.getPlainData();
				sCipherTime = niceCheck.getCipherDateTime();

				// 데이타를 추출합니다.
				HashMap mapresult = niceCheck.fnParse(sPlainData);
				sRequestNumber = (String)mapresult.get("REQ_SEQ");
				sErrorCode = (String)mapresult.get("ERR_CODE");
				sAuthType = (String)mapresult.get("AUTH_TYPE");

			} else if (iReturn == -1) {
				throw new BizException("복호화 시스템 오류입니다.");
			} else if (iReturn == -4) {
				throw new BizException("복호화 처리 오류입니다.");
			} else if (iReturn == -5) {
				throw new BizException("복호화 해쉬 오류입니다.");
			} else if (iReturn == -6) {
				throw new BizException("복호화 데이터 오류입니다.");
			} else if (iReturn == -9) {
				throw new BizException("입력 데이터 오류입니다.");
			} else if (iReturn == -12) {
				throw new BizException("사이트 패스워드 오류입니다.");
			} else {
				throw new BizException("알수 없는 에러 입니다. iReturn : " + iReturn);
			}
		} catch (BizException e) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
				"##################################### PASS 인증 오류 #####################################");
			stringBuilder.append(String.format("sRequestNumber : %s,", sRequestNumber));
			stringBuilder.append(String.format("sErrorCode : %s,", sErrorCode));
			stringBuilder.append(String.format("sAuthType : %s,", sAuthType));
			stringBuilder.append(String.format("sCipherTime : %s,", sCipherTime));
			stringBuilder.append(String.format("sMessage : %s,", sMessage));
			stringBuilder.append(
				"##################################### PASS 인증 오류 #####################################");
			log.error(stringBuilder.toString());
			notifyMessage.sendErrorMessage(e, stringBuilder);
		}
		return ResponseEntity.badRequest().body("PASS 인증 오류");
	}
}
