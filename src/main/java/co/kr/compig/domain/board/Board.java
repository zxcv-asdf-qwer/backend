package co.kr.compig.domain.board;

import co.kr.compig.api.board.dto.BoardDetailResponse;
import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.converter.BoardTypeConverter;
import co.kr.compig.common.code.converter.ContentsTypeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.file.SystemFile;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
  @Convert(converter = ContentsTypeConverter.class)
  private ContentsType contentsType; // 컨텐츠 유형

  @Column(length = 100)
  private String title; // 게시글 제목

  @Column
  private String smallTitle; // 소제목

  @Column
  private String contents; // 게시글 내용

  @Column
  @ColumnDefault("0")
  @Builder.Default
  private Integer viewCount = 0; // 조회수

  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y; // 게시글 상태

  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn pinYn = IsYn.N; // 상단 고정 여부

  @Column
  private LocalDateTime startDate; // 시작일

  @Column
  private LocalDateTime endDate; // 종료일

  private String thumbnailImageUrl;
  /* =================================================================
  * Domain mapping
  ================================================================= */
  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<SystemFile> systemFiles = new HashSet<>();

  /* =================================================================
  * Relation method
  ================================================================= */
  public void increaseViewCount() {
    this.viewCount++;
  }

  public void update(BoardUpdateRequest boardUpdateRequest) {
    this.boardType = boardUpdateRequest.getBoardType();
    this.contentsType = boardUpdateRequest.getContentsType();
    this.title = boardUpdateRequest.getTitle();
    this.smallTitle = boardUpdateRequest.getSmallTitle();
    this.contents = boardUpdateRequest.getContents();
    this.useYn = boardUpdateRequest.getUseYn();
    this.pinYn = boardUpdateRequest.getPinYn();
    this.startDate = boardUpdateRequest.getStartDate();
    this.endDate = boardUpdateRequest.getEndDate();
  }

  /* =================================================================
 * Default columns
 ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

  public BoardDetailResponse toBoardDetailResponse() {
    return BoardDetailResponse.builder()
        .boardId(this.id)
        .title(this.title)
        .smallTitle(this.smallTitle)
        .contents(this.contents)
        .boardType(this.boardType)
        .contentsType(this.contentsType)
        .viewCount(this.viewCount)
        .createdBy(this.createdAndModified.getCreatedBy())
        .startDate(this.startDate)
        .endDate(this.endDate)
        .thumbNail(this.thumbnailImageUrl)
        .systemFiles(this.systemFiles.stream().map(SystemFile::getFilePath).toList())
        .build();
  }
}
