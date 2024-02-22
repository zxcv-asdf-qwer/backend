package co.kr.compig.domain.post;

import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk01_board",
            columnNames = {"postId"})
    })
public class Post {

  @Id
  @Column(name = "post_id")
  private Long id; // 게시글 id

  @Column(length = 10)
  @Enumerated(EnumType.STRING)
  private PostType postType; // 게시글 유형

  @Column(length = 100)
  private String title; // 게시글 제목

  @Column
  private String content; // 게시글 내용

  @Column
  @ColumnDefault("0")
  @Builder.Default
  private Integer views = 0; // 조회수

  @Column(length = 50)
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus; // 게시글 상태

  @Column(length = 1)
  @ColumnDefault("'N'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn pinYn = IsYn.N; // 상단 고정 여부


  /* =================================================================
  * Domain mapping
  ================================================================= */

  /* =================================================================
  * Relation method
  ================================================================= */
  public void increaseViews(){
    this.views++;
  }

   /* =================================================================
  * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}
