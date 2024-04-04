package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiseaseCode implements BaseEnumCode<String> {
	DEMENTIA("DEM", "치매", UseYn.Y),
	BEDSORE("BED", "욕창", UseYn.Y),
	REHABILITATION("REH", "재활", UseYn.Y),
	STOMA("STO", "장루", UseYn.Y),
	ISOLATION("ISO", "격리", UseYn.Y),
	DIALYSIS("DIA", "투석", UseYn.Y),
	INFECTION("INF", "점염성 질환", UseYn.Y),
	PARALYSIS("PAR", "마비", UseYn.Y),
	DELIRIUM("DEL", "섬망", UseYn.Y),
	BODY_PARALYSIS("BOD", "전신마비", UseYn.Y),
	HEMIPLEGIA("HEM", "편마비", UseYn.Y),
	NOTHING("NOT", "질환 없음", UseYn.Y);

	private final String code;
	private final String desc;
	private final UseYn useYn;
}
