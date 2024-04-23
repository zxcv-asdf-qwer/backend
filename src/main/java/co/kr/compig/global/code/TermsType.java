package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType implements BaseEnumCode<String> {
	SERVICE_POLICY("SER", "서비스 이용약관"),
	PERSONAL_INFORMATION_POLICY("COL", "개인정보 수집 및 이용 동의"),
	PROCESSING_PERSONAL_DATA_POLICY("PRO", "개인정보 처리방침"),
	LOCATION_BASED_SERVICE_POLICY("LOC", "위치기반 서비스 이용약관"),
	THIRD_PARTY_INFORMED_CONSENT_POLICY("THI", "제 3자 정보 제공동의"),
	PUSH_NOTIFICATIONS_AGREE_POLICY("PUS", "푸시알림 수신동의"),
	RECEIVE_NOTIFICATION_AGREE_POLICY("REC", "알림톡 수신동의 "),
	SMS_AGREE_POLICY("SMS", "SMS 문자 수신 동의");

	private final String code;
	private final String desc;
}
