package co.kr.compig.api.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusCode implements BaseEnumCode<String> {
	POSTING("POS", "공고 게시중"), //공고 게시 중
	COMPLETE("COM", "공고 완료"), //공고 완료
	CANCEL("CAN", "공고 취소"); //공고 취소

	private final String code;
	private final String desc;
}
