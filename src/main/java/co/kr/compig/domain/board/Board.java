package co.kr.compig.domain.board;

import co.kr.compig.api.board.dto.BoardCreate;
import co.kr.compig.api.board.dto.BoardUpdate;
import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
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
            columnNames = {"boardId"})
    })
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id; // 게시글 id

  @Column(length = 10)
  @Enumerated(EnumType.STRING)
  private BoardType boardType; // 게시글 유형

  @Column(length = 100)
  private String title; // 게시글 제목

  @Column
  private String content; // 게시글 내용

  @Column
  @ColumnDefault("0")
  @Builder.Default
  private Integer views = 0; // 조회수

  @Column(length = 1)
  @ColumnDefault("'Y'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y; // 게시글 상태

  @Column(length = 1)
  @ColumnDefault("'N'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn pinYn = IsYn.N; // 상단 고정 여부

  public Board(BoardCreate boardCreate, Member member) {
    this.title = boardCreate.getTitle();
    this.content = boardCreate.getContent();
    this.boardType = boardCreate.getBoardType();
    this.pinYn = boardCreate.getPinYn();
    this.createdAndModified = new CreatedAndUpdated(member.getId(), LocalDateTime.now(), null, null);
  }


  /* =================================================================
  * Domain mapping
  ================================================================= */

  /* =================================================================
  * Relation method
  ================================================================= */
  public void increaseViews(){
    this.views++;
  }

  public void update(BoardUpdate boardUpdate) {
    this.title = boardUpdate.getTitle();
    this.content = boardUpdate.getContent();
    this.pinYn = boardUpdate.getPinYn();
    this.boardType = boardUpdate.getBoardType();
    this.useYn = boardUpdate.getUseYn();
  }

   /* =================================================================
  * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}
