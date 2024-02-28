package co.kr.compig.domain.file;

import co.kr.compig.common.code.FileType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.board.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk01_systemFile",
            columnNames = {"systemFileId"}
        )
    }
)
public class SystemFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "system_file_id")
  private Long id; // 파일 id

  @Column
  private String s3Path; // 파일 경로

  @Column
  private String fileNm; // 파일 이름

  @Column
  private FileType fileType; // 파일 타입

  @Column
  private String fileExtension; // 파일 확장자명

  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn latestYn = IsYn.Y; // 최신 여부

  @Column(length = 1)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y; // 사용 여부
/* =================================================================
  * Domain mapping
  ================================================================= */
  @JoinColumn(name = "board_id")
  @ManyToOne
  private Board board;

  /* =================================================================
 * Default columns
 ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}

