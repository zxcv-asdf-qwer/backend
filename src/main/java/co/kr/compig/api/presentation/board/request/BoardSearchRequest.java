package co.kr.compig.api.presentation.board.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.code.BoardType;
import co.kr.compig.api.domain.code.ContentsType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@SuperBuilder(toBuilder = true)
public class BoardSearchRequest extends PageableRequest {
	private BoardType boardType; // 게시판 유형
	private ContentsType contentsType; // 콘텐츠 유형
	private String title; // 제목
	private String smallTitle; // 소제목
	private String contents; // 내용
	private String createdBy; // 글쓴이
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate startDate; // 시작 날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate endDate; // 종료 날짜
}
