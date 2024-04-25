package co.kr.compig.api.presentation.board.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.board.Board;
import co.kr.compig.global.code.BoardType;
import co.kr.compig.global.code.ContentsType;
import co.kr.compig.global.code.IsYn;
import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "게시글 제목")
	private String title; // 게시글 제목

	@Length(min = 2, max = 100)
	@Schema(description = "소제목")
	private String smallTitle; // 소제목

	@NotBlank
	@Length(min = 2)
	@Schema(description = "게시글 내용")
	private String contents; // 게시글 내용

	@NotNull
	@Schema(description = "게시글 유형", example = "NOTICE")
	private BoardType boardType; // 게시글 유형

	@Schema(description = "콘텐츠 유형", example = "ACCOUNT")
	private ContentsType contentsType; // 콘텐츠 유형

	@Schema(description = "상단 고정 여부", example = "Y")
	private IsYn pinYn; // 상단 고정 여부

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "시작 날짜", example = "2024-04-25 09:43:04")
	private LocalDateTime startDate; // 시작 날짜

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "종료 날짜", example = "2024-04-26 09:43:04")
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
