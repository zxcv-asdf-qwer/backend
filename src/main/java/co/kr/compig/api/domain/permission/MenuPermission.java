package co.kr.compig.api.domain.permission;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.menu.Menu;
import co.kr.compig.api.presentation.permission.request.MenuPermissionUpdateRequest;
import co.kr.compig.api.presentation.permission.response.MenuPermissionDetailResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	name = "menu_permission_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "menu_permission_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class MenuPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_permission_seq_gen")
	@Column(name = "menu_permission_id")
	private Long id; // MenuPermission id

	@Column(length = 50)
	private String groupKey; // Group key

  /* =================================================================
   * Domain mapping
   ================================================================= */

	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk01_menu_permission"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member; // Member id

	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk02_menu_permission"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Menu menu; // Member

  /* =================================================================
   * Relation method
   ================================================================= */

	public void setMember(Member member) {
		this.member = member;
	}

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public MenuPermissionDetailResponse toMenuPermissionDetailResponse() {
		return MenuPermissionDetailResponse.builder()
			.id(this.id)
			.groupKey(this.groupKey)
			.memberId(this.member.getId())
			.userNm(this.member.getUserNm())
			.menuId(this.menu.getId())
			.menuNm(this.menu.getMenuNm())
			.build();
	}

	public void update(MenuPermissionUpdateRequest menuPermissionUpdateRequest, Member member, Menu menu) {
		this.member = member;
		this.menu = menu;
		this.groupKey = menuPermissionUpdateRequest.getGroupKey();

	}
}
