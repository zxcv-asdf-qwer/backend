package co.kr.compig.domain.permission;

import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.menu.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class MenuPermission {

  // MenuPermission id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long id;

  // Group key
  @Column(length = 50)
  private String groupKey;

    /* =================================================================
     * Domain mapping
     ================================================================= */

  // Member id
  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  // Member
  @JoinColumn(name = "menu_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Menu menu;

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


}
