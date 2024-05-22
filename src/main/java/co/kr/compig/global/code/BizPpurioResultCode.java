package co.kr.compig.global.code;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizPpurioResultCode implements BaseEnumCode<String> {
	AT_SEND("7000", "전달"),

	SMS_SEND("4100", "전달"),
	SMS_NO_COVERAGE("4400", "음영 지역"),
	SMS_DEVICE_OFF("4401", "단말기 전원 꺼짐"),
	SMS_STORAGE_EXCEEDED("4402", "단말기 메시지 저장 초과"),
	SMS_MESSAGE_DELETED("4403", "메시지 삭제 됨"),
	SMS_NO_LOCATION_INFO("4404", "가입자 위치 정보 없음"),
	SMS_DEVICE_BUSY("4405", "단말기 BUSY"),
	SMS_INVALID_NUMBER("4410", "잘못된 번호"),
	SMS_OTHER_ERROR("4420", "기타에러"),
	SMS_SPAM("4430", "스팸"),
	SMS_SEND_LIMIT_REJECTED("4431", "발송 제한 수신거부(스팸)"),
	SMS_NPDB_ERROR("4411", "NPDB 에러"),
	SMS_CALL_REJECTED("4412", "착신거절"),
	SMS_SMSC_FORMAT_ERROR("4413", "SMSC 형식오류"),
	SMS_NON_SUBSCRIBER("4414", "비가입자, 결번, 서비스정지"),
	SMS_IDENTIFICATION_CODE_ERROR("4415", "식별코드 오류"),
	SMS_TIMEOUT("4421", "타임아웃"),
	SMS_DEVICE_TEMPORARILY_SUSPENDED("4422", "단말기 일시정지"),
	SMS_DEVICE_REJECTED("4423", "단말기 착신거부"),
	SMS_UNSUPPORTED_URL_SMS("4424", "URL SMS 미지원폰"),
	SMS_DEVICE_CALL_PROCESSING("4425", "단말기 호 처리 중"),
	SMS_RETRY_LIMIT_EXCEEDED("4426", "재시도 한도 초과"),
	SMS_OTHER_DEVICE_PROBLEM("4427", "기타 단말기 문제"),
	SMS_SYSTEM_ERROR("4428", "시스템 에러"),
	SMS_PERSONAL_REPLY_NUMBER_BLOCKED("4432", "회신번호 차단(개인)"),
	SMS_CORPORATE_REPLY_NUMBER_BLOCKED("4433", "회신번호 차단(기업)"),
	SMS_UNREGISTERED_REPLY_NUMBER_BLOCKED("4434", "회신번호 사전 등록제에 의한 미등록 차단"),
	SMS_KISA_REPORTED_SPAM_REPLY_NUMBER("4435", "KISA 신고 스팸 회신 번호 차단"),
	SMS_REPLY_NUMBER_RULE_VIOLATION("4436", "회신번호 사전 등록제 번호규칙 위반"),
	SMS_DUPLICATE_SENDER_LIMIT("4437", "중복 발신 제한"),
	SMS_WHITELIST_TEXT_CHECK("4438", "화이트리스트 문구 체크"),
	SMS_KISA_SPAM_BLOCK("4443", "KISA 스팸 차단"),

	LMS_SEND("6600", "전달"),
	LMS_TIMEOUT("6601", "타임 아웃"),
	LMS_CALL_PROCESSING("6602", "핸드폰 호 처리 중"),
	LMS_NO_COVERAGE("6603", "음영 지역"),
	LMS_POWER_OFF("6604", "전원이 꺼져 있음"),
	LMS_STORAGE_EXCEEDED("6605", "메시지 저장 개수 초과"),
	LMS_INVALID_NUMBER("6606", "잘못된 번호"),
	LMS_TEMPORARY_SUSPENSION("6607", "서비스 일시 정지"),
	LMS_OTHER_DEVICE_PROBLEM("6608", "기타 단말기 문제"),
	LMS_CALL_REJECTED("6609", "착신 거절"),
	LMS_OTHER_ERROR("6610", "기타 에러"),
	LMS_SMC_FORMAT_ERROR("6611", "통신사의 SMC 형식 오류"),
	LMS_GATEWAY_FORMAT_ERROR("6612", "게이트웨이의 형식 오류"),
	LMS_UNSUPPORTED_DEVICE("6613", "서비스 불가 단말기"),
	LMS_CALL_UNAVAILABLE("6614", "핸드폰 호 불가 상태"),
	LMS_SMC_DELETED("6615", "SMC 운영자에 의해 삭제"),
	LMS_MESSAGE_QUEUE_EXCEEDED("6616", "통신사의 메시지 큐 초과"),
	LMS_CARRIER_SPAM_HANDLED("6617", "통신사의 스팸 처리"),
	LMS_FTC_SPAM_HANDLED("6618", "공정위의 스팸 처리"),
	LMS_GATEWAY_SPAM_HANDLED("6619", "게이트웨이의 스팸 처리"),
	LMS_SEND_LIMIT_EXCEEDED("6620", "발송 건수 초과"),
	LMS_MESSAGE_LENGTH_EXCEEDED("6621", "메시지의 길이 초과"),
	LMS_INVALID_NUMBER_FORMAT("6622", "잘못된 번호 형식"),
	LMS_INVALID_DATA_FORMAT("6623", "잘못된 데이터 형식"),
	LMS_MMS_INFO_NOT_FOUND("6624", "MMS 정보를 찾을 수 없음"),
	LMS_NPDB_ERROR("6625", "NPDB 에러"),
	LMS_REJECT_080("6626", "080 수신거부(SPAM)"),
	LMS_SEND_LIMIT_REJECTED_SPAM("6627", "발송 제한 수신거부(SPAM)"),
	LMS_PERSONAL_REPLY_NUMBER_BLOCKED("6628", "회신 번호 차단(개인)"),
	LMS_CORPORATE_REPLY_NUMBER_BLOCKED("6629", "회신 번호 차단(기업)"),
	LMS_UNSUPPORTED_NUMBER("6630", "서비스 불가 번호"),
	LMS_UNREGISTERED_REPLY_NUMBER_BLOCKED("6631", "회신 번호 사전 등록제에 의한 미등록 차단"),
	LMS_KISA_REPORTED_SPAM_REPLY_NUMBER("6632", "KISA 신고 스팸 회신 번호 차단"),
	LMS_REPLY_NUMBER_RULE_VIOLATION("6633", "회신 번호 사전 등록제 번호 규칙 위반"),
	LMS_ATTACHMENT_SIZE_EXCEEDED("6670", "첨부파일 사이즈 초과(60K)");

	private final String code;
	private final String desc;

	public static BizPpurioResultCode of(String dbData) {
		return dbData == null ? null : Arrays.stream(BizPpurioResultCode.values())
			.filter(e -> e.getCode().equals(dbData))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("Enum %s에 Code %s가 없습니다.", BizPpurioResultCode.class.getName(), dbData)));
	}
}
