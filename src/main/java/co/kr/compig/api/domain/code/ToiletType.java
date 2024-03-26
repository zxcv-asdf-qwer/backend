package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ToiletType implements BaseEnumCode<String> {
	DIAPERS("DIA", "기저귀", UseYn.Y),
	URINE_LINE("URI", "소변줄", UseYn.Y),
	STOMA_URINARY("STO", "장루/요루", UseYn.Y),
	SELF("SEL", "스스로 가능", UseYn.Y);

	private final String code;
	private final String desc;
	private final UseYn useYn;
}
