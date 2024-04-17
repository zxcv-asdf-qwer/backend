package co.kr.compig.api.presentation.board.response;

import co.kr.compig.global.code.FileType;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SystemFileResponse extends BaseAudit {
	private String filePath; // 파일 경로
	private String fileNm; // 파일 이름
	private FileType fileType; // 파일 타입
	private String fileExtension; // 파일 확장자명
	private IsYn latestYn; // 최신 여부
	private UseYn useYn; // 사용 여부
}
