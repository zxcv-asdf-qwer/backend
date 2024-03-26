package co.kr.compig.api.presentation.menu.request;

import co.kr.compig.api.domain.code.MenuDivCode;
import co.kr.compig.api.domain.code.MenuTypeCode;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.menu.Menu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {

	@NotNull
	private MenuDivCode menuDiv; // 메뉴 구분
	@NotBlank
	private String menuNm; // 메뉴명
	private String menuUrl; // 메뉴 URL
	private Integer seq; // 메뉴 순서
	@NotNull
	private MenuTypeCode menuType; // 메뉴 타입
	private UseYn useYn; // 사용 유무
	private Long parentMenuId; // 부모 메뉴 ID

	public Menu converterEntity(Menu parentMenu) {
		return Menu.builder()
			.menuDiv(this.menuDiv)
			.menuNm(this.menuNm)
			.menuUrl(this.menuUrl)
			.seq(this.seq)
			.menuType(this.menuType)
			.useYn(this.useYn)
			.parent(parentMenu)
			.build();
	}
}