package co.kr.compig.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@SequenceGenerator(
    name = "member_group_seq_gen", //시퀀스 제너레이터 이름
    sequenceName = "member_group_seq", //시퀀스 이름
    initialValue = 1, //시작값
    allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class MemberGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_group_seq_gen")
  @Column(name = "member_group_id")
  private Long id;

  @Column(length = 50, nullable = false)
  private String groupKey;

  @Column(length = 150, nullable = false)
  private String groupNm;

  @Column(length = 250, nullable = false)
  private String groupPath;

  /* =================================================================
   * Domain mapping
     ================================================================= */
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

  /* =================================================================
   * Business login
     ================================================================= */
  public void updateGroupInfo(String groupKey, String groupNm, String groupPath) {
    this.groupKey = groupKey;
    this.groupNm = groupNm;
    this.groupPath = groupPath;
  }
}
