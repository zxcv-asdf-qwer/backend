package co.kr.compig.api.presentation.board.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.board.Board;
import co.kr.compig.api.domain.code.BoardType;
import co.kr.compig.api.domain.code.ContentsType;
import co.kr.compig.api.domain.code.IsYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {

	@NotBlank
	@Length(min = 2, max = 100)
	private String title; // 게시글 제목

	@Length(min = 2, max = 100)
	private String smallTitle; // 소제목

	@NotBlank
	@Length(min = 2)
	private String contents; // 게시글 내용

	@NotNull
	private BoardType boardType; // 게시글 유형

	private ContentsType contentsType; // 콘텐츠 유형

	private IsYn pinYn; // 상단 고정 여부

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate; // 시작 날짜

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate; // 종료 날짜

	public Board converterEntity() {
		return Board.builder()
			.title(this.title)
			.smallTitle(this.smallTitle)
			.contents(this.contents)
			.boardType(this.boardType)
			.contentsType(this.contentsType)
			.pinYn(this.pinYn)
			.startDate(this.startDate)
			.endDate(this.endDate)
			.build();

	}
}
