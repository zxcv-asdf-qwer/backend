package co.kr.compig.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebasePushConfig {

  @Value("${api.fcm.care-partner.serviceKeyPath}")
  private String partnerServiceKeyPath;
  @Value("${api.fcm.care-partner.projectId}")
  private String partnerProjectId;
  @Value("${api.fcm.care-guardian.serviceKeyPath}")
  private String guardianServiceKeyPath;
  @Value("${api.fcm.care-guardian.projectId}")
  private String guardianProjectId;

  @Bean
  public FirebaseMessaging firebaseMessagingPartner() throws IOException {
    GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(partnerServiceKeyPath).getInputStream());
    FirebaseOptions firebaseOptions = FirebaseOptions
        .builder()
        .setCredentials(googleCredentials)
        .build();
    FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, partnerProjectId);
    return FirebaseMessaging.getInstance(app);
  }

  @Bean
  public FirebaseMessaging firebaseMessagingGuardian() throws IOException {
    GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(guardianServiceKeyPath).getInputStream());
    FirebaseOptions firebaseOptions = FirebaseOptions
        .builder()
        .setCredentials(googleCredentials)
        .build();
    FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, guardianProjectId);
    return FirebaseMessaging.getInstance(app);
  }
}
