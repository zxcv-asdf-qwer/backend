package co.kr.compig.api.domain.menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.code.MenuDivCode;
import co.kr.compig.api.domain.code.MenuTypeCode;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.converter.MenuDivCodeConverter;
import co.kr.compig.api.domain.code.converter.MenuTypeCodeConverter;
import co.kr.compig.api.domain.permission.MenuPermission;
import co.kr.compig.api.presentation.menu.request.MenuUpdateRequest;
import co.kr.compig.api.presentation.menu.response.MenuDetailResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "menu_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "menu_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq_gen")
	@Column(name = "menu_id")
	private Long id; // 메뉴코드

	@Column(nullable = false, length = 10)
	@Convert(converter = MenuDivCodeConverter.class)
	private MenuDivCode menuDiv; // 메뉴 구분

	@Column(nullable = false, length = 100)
	private String menuNm; // 메뉴명

	@Column(length = 150)
	private String menuUrl; // 메뉴 URL

	@Column(length = 3)
	private Integer seq; // 메뉴 순서

	@Column(nullable = false, length = 6)
	@Convert(converter = MenuTypeCodeConverter.class)
	private MenuTypeCode menuType; // 메뉴 타입

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용유무

	/* =================================================================
	 * Domain mapping
	 ================================================================= */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk01_menu"))
	private Menu parent;

	@Builder.Default
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Menu> child = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<MenuPermission> menuPermissions = new HashSet<>();

	/* =================================================================
	 * Relation method
	   ================================================================= */
	public void addChildMenu(final Menu menu) {
		this.child.add(menu);
		menu.setParent(this);
	}

	public void removeChildMenu(final Menu menu) {
		this.child.remove(menu);
		menu.setParent(null);
	}

	public void setParent(final Menu parent) {
		this.parent = parent;
	}

	public MenuDetailResponse toMenuDetailResponse() {
		return MenuDetailResponse.builder()
			.id(this.id)
			.menuDiv(this.menuDiv)
			.menuNm(this.menuNm)
			.menuUrl(this.menuUrl)
			.seq(this.seq)
			.menuType(this.menuType)
			.useYn(this.useYn)
			.child(this.child.stream()
				.map(Menu::toMenuDetailResponse)
				.collect(Collectors.toList())) // Convert child Menu list
			.build();
	}

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public void update(MenuUpdateRequest menuUpdateRequest, Menu parent) {
		this.menuDiv = menuUpdateRequest.getMenuDiv();
		this.menuNm = menuUpdateRequest.getMenuNm();
		this.menuUrl = menuUpdateRequest.getMenuUrl();
		this.seq = menuUpdateRequest.getSeq();
		this.menuType = menuUpdateRequest.getMenuType();
		this.useYn = menuUpdateRequest.getUseYn();
		this.parent = parent;
	}


  /* =================================================================
   * Business
     ================================================================= */

}
