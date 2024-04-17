package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentsType implements BaseEnumCode<String> {
	ACCOUNT("ACCOUNT", "board.contents.type.account", UseYn.Y),  // 계정
	INSPECTION("INSPECTION", "board.contents.type.inspection", UseYn.N), // 점검
	EVENT("EVENT", "board.contents.type.event", UseYn.N),    // 이벤트
	ETC("ETC", "board.contents.type.etc", UseYn.Y),   // 기타

	// FAQ 관리
	MATCH_BEFORE("MBE", "매칭 서비스 이용 전(환자, 보호자)", UseYn.Y), // 매칭 서비스 이용 전(환자, 보호자)
	MATCH_AFTER("MAF", "매칭 서비스 이용 중(환자, 보호자)", UseYn.Y), // 매칭 서비스 이용 중(환자, 보호자)
	PAYMENT("PAY", "간병비 결제", UseYn.Y), // 간병비 결제
	MATCH_SERVICE("MSE", "매칭 서비스(케어 매니저)", UseYn.Y), // 매칭 서비스(케어 매니저)
	CALCULATE("CAL", "정산(케어 매니저)", UseYn.Y), // 정산(케어 매니저)
	EDUCATION("EDU", "교육 및 자격증(케어 매니저)", UseYn.Y), // 교육 및 자격증(케어 매니저)

	// 교육
	PRIMARY_EDUCATION("PED", "간병인 필수 교육", UseYn.Y), // 간병인 필수 교육
	CERTIFICATE_EDUCATION("CED", "간병인 자격증 교육", UseYn.Y), // 간병인 자격증 교육
	DEEP_EDUCATION("DED", "간병 심화 교육", UseYn.Y), // 간병 심화 교육

	// 이벤트
	PROGRESS_EVENT("PRO", "진행중인 이벤트", UseYn.Y),
	END_EVENT("END", "종료된 이벤트", UseYn.Y);

	private final String code;
	private final String desc;
	private final UseYn useYn;
}
