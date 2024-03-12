package co.kr.compig.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.common.config.jackson.CustomObjectMapper;
import co.kr.compig.common.exception.CommonExceptionHandler;
import co.kr.compig.common.interceptor.PagingInterceptor;
import co.kr.compig.common.utils.ApplicationContextUtil;

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
