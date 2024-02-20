package co.kr.compig.domain.menu;

import co.kr.compig.common.code.MenuDivCode;
import co.kr.compig.common.code.MenuTypeCode;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.converter.MenuDivCodeConverter;
import co.kr.compig.common.code.converter.MenuTypeCodeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Menu {

  // 메뉴코드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  // 메뉴명
  @Column(nullable = false, length = 100)
  private String menuNm;

  // 영문 메뉴명
  @Column(length = 100)
  private String menuNmEn;

  // 중문 메뉴명
  @Column(length = 100)
  private String menuNmCn;

  // 메뉴 URL
  @Column(length = 150)
  private String menuUrl;

  // 메뉴 구분
  @Column(nullable = false, length = 10)
  @Convert(converter = MenuDivCodeConverter.class)
  private MenuDivCode menuDiv;

  // 메뉴 레벨
  @Column(length = 1)
  private Integer menuLvl;

  // 메뉴 순서
  @Column(length = 3)
  private Integer seq;

  // 메뉴 타입
  @Column(nullable = false, length = 6)
  @Convert(converter = MenuTypeCodeConverter.class)
  private MenuTypeCode menuType;

  // 사용유무
  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y;

  // 대메뉴 아이콘 css class
  @Column(length = 150)
  private String iconCls;

  //사용자메뉴얼 국문
  @Column(length = 500)
  private String manualKoAttachUrl;

  //사용자메뉴얼 영문
  @Column(length = 500)
  private String manualEnAttachUrl;
  /* =================================================================
   * Domain mapping
   ================================================================= */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @Builder.Default
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Menu> child = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Role> roles = new HashSet<>();

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

  /* =================================================================
   * Default columns
   ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

    /* =================================================================
     * Business login
     ================================================================= */

}
