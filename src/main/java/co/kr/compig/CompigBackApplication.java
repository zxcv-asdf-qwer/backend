package co.kr.compig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableFeignClients
@SpringBootApplication
public class CompigBackApplication {
	// @PostConstruct
	// public void started() {
	// 	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	// }

	public static void main(String[] args) {
		SpringApplication.run(CompigBackApplication.class, args);
	}

}
