package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankCode implements BaseEnumCode<String> {
	KDB("002", "산업은행", UseYn.Y),
	IBK("003", "기업은행", UseYn.Y),
	KB("004", "국민은행", UseYn.Y),
	SUHYUP("007", "수협은행", UseYn.Y),
	NH_NONGHYUP("011", "NH농협은행", UseYn.Y),
	NONGHYUP("012", "지역농축협", UseYn.Y),
	WOORI("020", "우리은행", UseYn.Y),
	SC("023", "SC제일은행", UseYn.Y),
	CITY("027", "한국씨티은행", UseYn.Y),
	DAEGU("031", "대구은행", UseYn.Y),
	BUSAN("032", "부산은행", UseYn.Y),
	GWANGJU("034", "광주은행", UseYn.Y),
	JEJU("035", "제주은행", UseYn.Y),
	JEONBUK("037", "전북은행", UseYn.Y),
	GYEONGNAM("039", "경남은행", UseYn.Y),
	KFCC("045", "새마을금고", UseYn.Y),
	SINHYUP("048", "신협은행", UseYn.Y),
	BABILLOAN("050", "저축은행", UseYn.Y),
	HSBC("054", "HSBC은행", UseYn.Y),
	DOICH("055", "도이치은행", UseYn.Y),
	JPMORGAN("057", "제이피모간체이스은행", UseYn.Y),
	BOA("060", "BOA은행", UseYn.Y),
	BNP("061", "비엔피파리바은행", UseYn.Y),
	ICBC("062", "중국공상은행", UseYn.Y),
	BFCF("064", "산림조합중앙회", UseYn.Y),
	CHINA_CONSTRUCTION("067", "중국건설은행", UseYn.Y),
	POST("071", "우체국은행", UseYn.Y),
	HANA("081", "하나은행", UseYn.Y),
	SHINHAN("088", "신한은행", UseYn.Y),
	KBANK("089", "케이뱅크", UseYn.Y),
	KAKAO("090", "카카오뱅크", UseYn.Y),
	MYASSET("209", "유안타증권", UseYn.Y),
	KBSEC("218", "KB증권", UseYn.Y),
	KTB("227", "다올투자증권", UseYn.Y),
	MIRAE("238", "미래에셋증권", UseYn.Y),
	SAMSUNG("240", "삼성증권", UseYn.Y),
	SECURITIES("243", "한국투자증권", UseYn.Y),
	NHPV("247", "NH투자증권", UseYn.Y),
	KYOBO("261", "교보증권", UseYn.Y),
	HIIB("262", "하이투자증권", UseYn.Y),
	HMSEC("263", "현대차증권", UseYn.Y),
	KIWOOM("264", "키움은행", UseYn.Y),
	;
	private final String code;
	private final String desc;
	private final UseYn useYn;

	public static BankCode of(String bankName) {
		if (bankName == null) {
			throw new IllegalArgumentException();
		}
		for (BankCode bc : BankCode.values()) {
			if (bc.code.equals(bankName)) {
				return bc;
			}
		}
		throw new IllegalArgumentException("일치하는 은행 코드가 없습니다.");
	}
}
