package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationType implements BaseEnumCode<String> {
	HOME("HOM", "집", UseYn.Y),
	HOSPITAL("HOS", "병의원", UseYn.Y);

	private final String code;
	private final String desc;
	private final UseYn useYn;
}
