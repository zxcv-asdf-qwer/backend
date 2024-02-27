package co.kr.compig.domain.menu;

import co.kr.compig.common.code.MenuDivCode;
import co.kr.compig.common.code.MenuTypeCode;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.converter.MenuDivCodeConverter;
import co.kr.compig.common.code.converter.MenuTypeCodeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.permission.MenuPermission;
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
import jakarta.persistence.Table;
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
@Table(name = "menu_test")
public class Menu {

  // 메뉴코드
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  // 메뉴 구분
  @Column(nullable = false, length = 10)
  @Convert(converter = MenuDivCodeConverter.class)
  private MenuDivCode menuDiv;

  // 메뉴명
  @Column(nullable = false, length = 100)
  private String menuNm;

  // 메뉴 URL
  @Column(length = 150)
  private String menuUrl;

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
