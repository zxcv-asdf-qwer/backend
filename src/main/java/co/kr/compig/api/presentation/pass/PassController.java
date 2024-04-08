package co.kr.compig.api.presentation.pass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import NiceID.Check.CPClient;
import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.pass.request.PassSaveRequest;
import co.kr.compig.api.presentation.pass.response.PassResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	private final MemberService memberService;

	@GetMapping(value = "/test")
	public String getPassTestPage1(HttpServletRequest request, HttpServletResponse response,
		ModelMap modelMap) {
		CPClient niceCheck = new CPClient();

		String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
		String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드

		String sRequestNumber = niceCheck.getRequestNO(sSiteCode);        // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로

		String sAuthType = "";        // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
		String customize = "";        // 없으면 기본 웹페이지 / Mobile : 모바일페이지

		String sReturnUrl = "http://localhost:8080/pass/success"; // 성공시 이동될 URL
		String sErrorUrl = "http://localhost:8080/pass/fail"; // 실패시 이동될 URL

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

		modelMap.addAttribute("sMessage", sMessage);
		modelMap.addAttribute("sEncData", sEncData);
		log.error("##################################### PASS 인증 오류 #####################################");
		log.error(String.format("sPlainData : %s,", sPlainData));
		log.error("##################################### PASS 인증 오류 #####################################");

		return "pass_test";
	}

	@RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
	public String passSuccess1(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		CPClient niceCheck = new CPClient();

		String sEncodeData = request.getParameter("EncodeData");

		String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
		String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드

		String sCipherTime = "";            // 복호화한 시간
		String sRequestNumber = "";            // 요청 번호
		String sResponseNumber = "";        // 인증 고유번호
		String sAuthType = "";                // 인증 수단
		String sMessage = "";
		String sPlainData = "";

		int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
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
				sMessage = "세션값 불일치 오류입니다.";
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

			modelMap.addAttribute("dto", passResponse);

		} else if (iReturn == -1) {
			sMessage = "복호화 시스템 오류입니다.";
		} else if (iReturn == -4) {
			sMessage = "복호화 처리 오류입니다.";
		} else if (iReturn == -5) {
			sMessage = "복호화 해쉬 오류입니다.";
		} else if (iReturn == -6) {
			sMessage = "복호화 데이터 오류입니다.";
		} else if (iReturn == -9) {
			sMessage = "입력 데이터 오류입니다.";
		} else if (iReturn == -12) {
			sMessage = "사이트 패스워드 오류입니다.";
		} else {
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}
		modelMap.addAttribute("sMessage", sMessage);
		log.error("##################################### PASS 인증 오류 #####################################");
		log.error(String.format("sRequestNumber : %s,", sRequestNumber));
		log.error(String.format("sResponseNumber : %s,", sResponseNumber));
		log.error(String.format("sAuthType : %s,", sRequestNumber));
		log.error(String.format("sCipherTime : %s,", sCipherTime));
		log.error(String.format("sMessage : %s,", sMessage));
		log.error("##################################### PASS 인증 오류 #####################################");

		return "pass_success";
	}

	@RequestMapping(value = "/fail", method = {RequestMethod.GET, RequestMethod.POST})
	public String passFail1(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
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

		int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
		if (iReturn == 0) {
			sPlainData = niceCheck.getPlainData();
			sCipherTime = niceCheck.getCipherDateTime();

			// 데이타를 추출합니다.
			HashMap mapresult = niceCheck.fnParse(sPlainData);
			sRequestNumber = (String)mapresult.get("REQ_SEQ");
			sErrorCode = (String)mapresult.get("ERR_CODE");
			sAuthType = (String)mapresult.get("AUTH_TYPE");

		} else if (iReturn == -1) {
			sMessage = "복호화 시스템 오류입니다.";
		} else if (iReturn == -4) {
			sMessage = "복호화 처리 오류입니다.";
		} else if (iReturn == -5) {
			sMessage = "복호화 해쉬 오류입니다.";
		} else if (iReturn == -6) {
			sMessage = "복호화 데이터 오류입니다.";
		} else if (iReturn == -9) {
			sMessage = "입력 데이터 오류입니다.";
		} else if (iReturn == -12) {
			sMessage = "사이트 패스워드 오류입니다.";
		} else {
			sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
		}
		modelMap.addAttribute("sMessage", sMessage);
		log.error("##################################### PASS 인증 오류 #####################################");
		log.error(String.format("sRequestNumber : %s,", sRequestNumber));
		log.error(String.format("sErrorCode : %s,", sErrorCode));
		log.error(String.format("sAuthType : %s,", sAuthType));
		log.error(String.format("sCipherTime : %s,", sCipherTime));
		log.error(String.format("sMessage : %s,", sMessage));
		log.error("##################################### PASS 인증 오류 #####################################");

		return "pass_fail";
	}
	//
	// @GetMapping(value = "/test")
	// public ResponseEntity<Response<?>> getPassTestPage(HttpServletRequest request, HttpServletResponse response,
	// 	ModelMap modelMap) {
	// 	CPClient niceCheck = new CPClient();
	//
	// 	String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
	// 	String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드
	//
	// 	String sRequestNumber = niceCheck.getRequestNO(sSiteCode);        // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
	//
	// 	String sAuthType = "";        // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	// 	String customize = "";        // 없으면 기본 웹페이지 / Mobile : 모바일페이지
	//
	// 	String sReturnUrl = "http://localhost:8080/pass/success"; // 성공시 이동될 URL
	// 	String sErrorUrl = "http://localhost:8080/pass/fail"; // 실패시 이동될 URL
	//
	// 	// 입력될 plain 데이타를 만든다.
	// 	String sPlainData =
	// 		"7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
	// 			"8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
	// 			"9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
	// 			"7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
	// 			"7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
	// 			"9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
	//
	// 	String sMessage = "";
	// 	String sEncData = "";
	//
	// 	int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
	// 	if (iReturn == 0) {
	// 		sEncData = niceCheck.getCipherData();
	// 	} else if (iReturn == -1) {
	// 		sMessage = "암호화 시스템 에러입니다.";
	// 	} else if (iReturn == -2) {
	// 		sMessage = "암호화 처리오류입니다.";
	// 	} else if (iReturn == -3) {
	// 		sMessage = "암호화 데이터 오류입니다.";
	// 	} else if (iReturn == -9) {
	// 		sMessage = "입력 데이터 오류입니다.";
	// 	} else {
	// 		sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	// 	}
	//
	// 	request.getSession().setAttribute("REQ_SEQ", sRequestNumber);
	//
	// 	// modelMap.addAttribute("sMessage", sMessage);
	// 	// modelMap.addAttribute("sEncData", sEncData);
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	// 	log.error(String.format("sPlainData : %s,", sPlainData));
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	//
	// 	return ResponseEntity.ok().body(Response.builder().data(Map.of("passUrl", sEncData)).build());
	// 	// return "pass_test";
	// }
	//
	// @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
	// public ResponseEntity<?> passSuccess(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
	// 	CPClient niceCheck = new CPClient();
	//
	// 	String sEncodeData = request.getParameter("EncodeData");
	//
	// 	String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
	// 	String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드
	//
	// 	String sCipherTime = "";            // 복호화한 시간
	// 	String sRequestNumber = "";            // 요청 번호
	// 	String sResponseNumber = "";        // 인증 고유번호
	// 	String sAuthType = "";                // 인증 수단
	// 	String sMessage = "";
	// 	String sPlainData = "";
	//
	// 	int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
	// 	// 추출한 정보를 이용하여 redirect URL 생성
	// 	String redirectUrlWithParams = clientUrl + "?";
	// 	if (iReturn == 0) {
	// 		sPlainData = niceCheck.getPlainData();
	// 		sCipherTime = niceCheck.getCipherDateTime();
	//
	// 		// 데이타를 추출합니다.
	// 		HashMap mapresult = niceCheck.fnParse(sPlainData);
	// 		sRequestNumber = (String)mapresult.get("REQ_SEQ");
	// 		sResponseNumber = (String)mapresult.get("RES_SEQ");
	// 		sAuthType = (String)mapresult.get("AUTH_TYPE");
	//
	// 		String session_sRequestNumber = (String)request.getSession().getAttribute("REQ_SEQ");
	// 		if (!sRequestNumber.equals(session_sRequestNumber)) {
	// 			sMessage = "세션값 불일치 오류입니다.";
	// 		}
	//
	// 		PassResponse passResponse = PassResponse.builder()
	// 			.sRequestNumber(sRequestNumber)
	// 			.sResponseNumber(sResponseNumber)
	// 			.sAuthType(sAuthType)
	// 			.sCipherTime(sCipherTime)
	// 			.name((String)mapresult.get("NAME"))
	// 			.birth((String)mapresult.get("BIRTHDATE"))
	// 			.gender((String)mapresult.get("GENDER"))
	// 			.nationalInfo((String)mapresult.get("NATIONAINFO"))
	// 			.dupInfo((String)mapresult.get("DI"))
	// 			.connInfo((String)mapresult.get("CI"))
	// 			.phone((String)mapresult.get("MOBILE_NO"))
	// 			.mobileCompany((String)mapresult.get("MOBILE_CO"))
	// 			.build();
	//
	// 		// modelMap.addAttribute("dto", passResponse);
	// 		redirectUrlWithParams += "requestnumber=" + passResponse.getSRequestNumber()
	// 			+ "&responsenumber=" + passResponse.getSResponseNumber()
	// 			+ "&authtype=" + passResponse.getSAuthType()
	// 			+ "&name=" + passResponse.getName()
	// 			+ "&birthdate=" + passResponse.getBirth()
	// 			+ "&gender=" + passResponse.getGender()
	// 			+ "&nationalInfo=" + passResponse.getNationalInfo()
	// 			+ "&dupInfo=" + passResponse.getDupInfo()
	// 			+ "&connInfo=" + passResponse.getConnInfo()
	// 			+ "&mobile=" + passResponse.getPhone()
	// 			+ "&mobileCo=" + passResponse.getMobileCompany();
	//
	// 	} else if (iReturn == -1) {
	// 		sMessage = "복호화 시스템 오류입니다.";
	// 	} else if (iReturn == -4) {
	// 		sMessage = "복호화 처리 오류입니다.";
	// 	} else if (iReturn == -5) {
	// 		sMessage = "복호화 해쉬 오류입니다.";
	// 	} else if (iReturn == -6) {
	// 		sMessage = "복호화 데이터 오류입니다.";
	// 	} else if (iReturn == -9) {
	// 		sMessage = "입력 데이터 오류입니다.";
	// 	} else if (iReturn == -12) {
	// 		sMessage = "사이트 패스워드 오류입니다.";
	// 	} else {
	// 		sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	// 	}
	// 	// modelMap.addAttribute("sMessage", sMessage);
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	// 	log.error("##################################### /pass/success #####################################");
	// 	log.error(String.format("sRequestNumber : %s,", sRequestNumber));
	// 	log.error(String.format("sResponseNumber : %s,", sResponseNumber));
	// 	log.error(String.format("sAuthType : %s,", sRequestNumber));
	// 	log.error(String.format("sCipherTime : %s,", sCipherTime));
	// 	log.error(String.format("sMessage : %s,", sMessage));
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	//
	// 	return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, redirectUrlWithParams).build();
	//
	// 	// return "pass_success";
	// }
	//
	// @RequestMapping(value = "/fail", method = {RequestMethod.GET, RequestMethod.POST})
	// public ResponseEntity<?> passFail(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
	// 	CPClient niceCheck = new CPClient();
	//
	// 	String sEncodeData = request.getParameter("EncodeData");
	//
	// 	String sSiteCode = NICE_PASS_SITE_CODE;            // NICE로부터 부여받은 사이트 코드
	// 	String sSitePassword = NICE_PASS_SITE_PW;        // NICE로부터 부여받은 사이트 패스워드
	//
	// 	String sCipherTime = "";            // 복호화한 시간
	// 	String sRequestNumber = "";         // 요청 번호
	// 	String sErrorCode = "";                // 인증 결과코드
	// 	String sAuthType = "";              // 인증 수단
	// 	String sMessage = "";
	// 	String sPlainData = "";
	//
	// 	int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
	// 	// 추출한 정보를 이용하여 redirect URL 생성
	// 	String redirectUrlWithParams = clientUrl + "?";
	// 	if (iReturn == 0) {
	// 		sPlainData = niceCheck.getPlainData();
	// 		sCipherTime = niceCheck.getCipherDateTime();
	//
	// 		// 데이타를 추출합니다.
	// 		HashMap mapresult = niceCheck.fnParse(sPlainData);
	// 		sRequestNumber = (String)mapresult.get("REQ_SEQ");
	// 		sErrorCode = (String)mapresult.get("ERR_CODE");
	// 		sAuthType = (String)mapresult.get("AUTH_TYPE");
	//
	// 	} else if (iReturn == -1) {
	// 		sMessage = "복호화 시스템 오류입니다.";
	// 	} else if (iReturn == -4) {
	// 		sMessage = "복호화 처리 오류입니다.";
	// 	} else if (iReturn == -5) {
	// 		sMessage = "복호화 해쉬 오류입니다.";
	// 	} else if (iReturn == -6) {
	// 		sMessage = "복호화 데이터 오류입니다.";
	// 	} else if (iReturn == -9) {
	// 		sMessage = "입력 데이터 오류입니다.";
	// 	} else if (iReturn == -12) {
	// 		sMessage = "사이트 패스워드 오류입니다.";
	// 	} else {
	// 		sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	// 	}
	// 	redirectUrlWithParams += "error=true";
	// 	// modelMap.addAttribute("sMessage", sMessage);
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	// 	log.error("##################################### /pass/fail #####################################");
	// 	log.error(String.format("sRequestNumber : %s,", sRequestNumber));
	// 	log.error(String.format("sErrorCode : %s,", sErrorCode));
	// 	log.error(String.format("sAuthType : %s,", sAuthType));
	// 	log.error(String.format("sCipherTime : %s,", sCipherTime));
	// 	log.error(String.format("sMessage : %s,", sMessage));
	// 	log.error("##################################### PASS 인증 오류 #####################################");
	//
	// 	return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, redirectUrlWithParams).build();
	//
	// 	// return "pass_fail";
	// }

	@PutMapping(value = "/pass")
	public ResponseEntity<Response<Map<String, String>>> pass(@RequestBody @Valid PassSaveRequest passSaveRequest) {

		Member memberById = memberService.getMemberById(SecurityUtil.getMemberId());
		memberById.passUpdate(passSaveRequest);
		return ResponseEntity.ok()
			.body(Response.<Map<String, String>>builder().data(Map.of("memberId", memberById.getId())).build());
	}
}