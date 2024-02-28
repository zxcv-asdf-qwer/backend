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

  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member; // Member id

  @JoinColumn(name = "menu_id")
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


}
