package co.kr.compig.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk01_member_group", columnNames = {"groupKey", "member_id"})
    }
)
public class MemberGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_group_id")
  private Long id;

  @Column(length = 50, nullable = false)
  private String groupKey;

  @Column(length = 150, nullable = false)
  private String groupNm;

  @Column(length = 250, nullable = false)
  private String groupPath;

  @Builder.Default
  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member = new Member();

  /* =================================================================
   * Relation method
   ================================================================= */

  public void setMember(Member member) {
    this.member = member;
  }

}
