package co.kr.compig.api.application.info.push.model;

import com.google.firebase.messaging.Notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record NotificationRequest(@NotBlank String deviceToken,
								  String title,
								  String body, String link) {

	@Builder
	public NotificationRequest {
	}

	public Notification toNotification() {
		return Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build();
	}
}