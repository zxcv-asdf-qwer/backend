package co.kr.compig.api.application.info.push;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import co.kr.compig.api.application.info.push.model.NotificationRequest;
import co.kr.compig.api.presentation.push.request.PushCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebasePushService {

	private final FirebaseMessaging firebaseMessaging;

	private final PushService pushService;
	private final String appSchema = "compig://compig";

	public String sendMessageTo(NotificationRequest notificationRequest) {
		Message message = makeMessage(notificationRequest);
		pushService.create(PushCreate.builder()
			.deviceUuid(notificationRequest.deviceToken())
			.title(notificationRequest.title())
			.contents(notificationRequest.body())
			.build());

		try {
			String response = firebaseMessaging.send(message);
			log.debug("### Firebase push response : {}", response);
			return response;
		} catch (FirebaseMessagingException e) {
			log.error("Firebase 전송중 에러 발생 : ", e);
		}

		return null;
	}

	private Message makeMessage(NotificationRequest notificationRequest) {
		Notification notification = notificationRequest.toNotification();

		Map<String, String> putData = new HashMap<>();
		putData.put("title", notificationRequest.title());
		putData.put("body", notificationRequest.body());
		putData.put("link", appSchema + "/" + notificationRequest.link());

		Message message = Message
			.builder()
			.setToken(notificationRequest.deviceToken())
			.setNotification(notification)
			.putAllData(putData)
			.build();

		return message;

	}
}
