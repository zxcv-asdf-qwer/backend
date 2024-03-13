package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType implements BaseEnumCode<String> {
	THUMBNAIL("TMN", "썸네일", UseYn.Y),
	BOARD("BOA", "게시판", UseYn.Y);

	private final String code;
	private final String desc;
	private final UseYn useYn;
}
