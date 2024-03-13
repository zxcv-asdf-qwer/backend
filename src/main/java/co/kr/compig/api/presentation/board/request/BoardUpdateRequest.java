package co.kr.compig.api.presentation.board.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.code.BoardType;
import co.kr.compig.api.domain.code.ContentsType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.UseYn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

	private BoardType boardType;
	private ContentsType contentsType;
	private String title;
	private String smallTitle;
	private String contents;
	private UseYn useYn;
	private IsYn pinYn;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;

}
