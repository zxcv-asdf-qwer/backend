package co.kr.compig.api.presentation.push.request;

import java.time.LocalDateTime;

import co.kr.compig.api.domain.push.Push;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCreate {

	private String deviceUuid;
	private String title;
	private String contents;
	private LocalDateTime sendTime;

	public Push convertEntity() {
		Push push = Push.builder()
			.title(this.title)
			.message(this.contents)
			.sendTime(this.sendTime)
			.deviceUuid(this.deviceUuid)
			.build();
		push.setDefaultCreated();
		return push;
	}

}
