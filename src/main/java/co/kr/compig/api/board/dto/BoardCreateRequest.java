package co.kr.compig.api.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.domain.board.Board;
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

	private List<String> imageUrlList;

	private String thumbnailImageUrl;

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
			.thumbnailImageUrl(thumbnailImageUrl)
			.build();

	}

	public void setImageUrlListAndThumbnail(List<String> imageUrlList, int thumbnailIndex) {
		this.imageUrlList = imageUrlList;
		if (thumbnailIndex >= 0 && thumbnailIndex < imageUrlList.size()) {
			this.thumbnailImageUrl = imageUrlList.get(thumbnailIndex);
		}
	}

	public void setThumbnailImageUrl(List<SystemFileResponse> imageUrlList, int thumbnailIndex) {
		List<String> imageUrls = new ArrayList<>();
		for (SystemFileResponse systemFileResponse : imageUrlList) {
			imageUrls.add(systemFileResponse.getFilePath());
		}
		this.imageUrlList = imageUrls;
		if (thumbnailIndex >= 0 && thumbnailIndex < imageUrlList.size()) {
			this.thumbnailImageUrl = imageUrlList.get(thumbnailIndex).getFilePath();
		}
	}
}
