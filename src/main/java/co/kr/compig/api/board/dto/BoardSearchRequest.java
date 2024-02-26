package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.dto.PageableRequest;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BoardSearchRequest extends PageableRequest {

  private BoardType boardType; // 게시판 유형
  private ContentsType contentsType; // 콘텐츠 유형
  private String title; // 제목
  private String smallTitle; // 소제목
  private String contents; // 내용
  private String createdBy; // 글쓴이
  private LocalDate startDate; // 시작 날짜
  private LocalDate endDate; // 종료 날짜
}