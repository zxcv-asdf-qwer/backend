package co.kr.compig.api.infrastructure.auth.social.apple.feign;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Configuration 붙이지 말 것 컴포넌트 스캔에서 제외 시키고 타겟 FeignClient만 쓰게 해야함
//TODO refactoring ErrorDecoder exception custom
public class AppleFeignConfig {

	@Bean
	public AppleFeignClientErrorDecoder appleFeignClientErrorDecoder() {
		return new AppleFeignClientErrorDecoder(new ObjectMapper());
	}
}
