package co.kr.compig.domain.board;

import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.converter.BoardTypeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
            columnNames = {"boardId"})
    })
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id; // 게시글 id

  @Column(length = 10)
  @Convert(converter = BoardTypeConverter.class)
  private BoardType boardType; // 게시글 유형

  @Column(length = 10)
  @Convert(converter = BoardTypeConverter.class)
  private ContentsType contentsType; // 컨텐츠 유형

  @Column(length = 100)
  private String title; // 게시글 제목

  @Column
  private String contents; // 게시글 내용

  @Column
  @ColumnDefault("0")
  @Builder.Default
  private Integer views = 0; // 조회수

  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y; // 게시글 상태

  @Column(length = 1)
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

  public void update(BoardUpdateRequest boardUpdateRequest) {
    this.title = boardUpdateRequest.getTitle();
    this.contents = boardUpdateRequest.getContents();
    this.pinYn = boardUpdateRequest.getPinYn();
    this.boardType = boardUpdateRequest.getBoardType();
    this.useYn = boardUpdateRequest.getUseYn();
  }

   /* =================================================================
  * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}
