package co.kr.compig.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.global.config.jackson.CustomObjectMapper;
import co.kr.compig.global.error.handler.CommonExceptionHandler;
import co.kr.compig.global.interceptor.PagingInterceptor;
import co.kr.compig.global.utils.ApplicationContextUtil;

@Import(value = {CommonExceptionHandler.class, ApplicationContextUtil.class})
@Configuration
public class CommonConfig {

	@Bean
	@Primary
	public ObjectMapper commonObjectMapper() {
		return CustomObjectMapper.getObjectMapper();
	}

	@Bean
	public PagingInterceptor commonPagingInterceptor() {
		return new PagingInterceptor();
	}

}
