package co.kr.compig.api.board.dto;

import java.time.LocalDateTime;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;

public class BoardBaseResponse {
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

}
