package co.kr.compig.api.application.info.push.model;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
	@NotNull
	private NoticeCode noticeCode;
	private String email;
	private List<String> targetTokens;
	private String phoneNumber;
	private Map<String, Object> data; //변하는 데이터
}
