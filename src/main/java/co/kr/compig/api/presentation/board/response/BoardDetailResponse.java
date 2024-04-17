package co.kr.compig.api.presentation.board.response;

import java.time.LocalDateTime;
import java.util.List;

import co.kr.compig.global.code.BoardType;
import co.kr.compig.global.code.ContentsType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BoardDetailResponse extends BaseAudit {

	private Long boardId; // 게시글 id
	private String title; // 게시글 제목
	private String smallTitle; // 게시글 소제목
	private String contents; // 게시글 내용
	private BoardType boardType; // 게시글 유형
	private ContentsType contentsType; // 콘텐츠 유형
	private Integer viewCount; // 조회수
	private String createdBy; // 작성자
	private LocalDateTime startDate; // 시작 날짜
	private LocalDateTime endDate; // 종료 날짜
	private String thumbNail; // 썸네일
	private List<String> systemFiles; // 올린 파일

}
