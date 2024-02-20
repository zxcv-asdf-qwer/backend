package co.kr.compig.common.config;


import co.kr.compig.common.config.jackson.CustomObjectMapper;
import co.kr.compig.common.interceptor.PagingInterceptor;
import co.kr.compig.common.util.ApplicationContextUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Import(value = {ApplicationContextUtil.class, JpaConfig.class})
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

  @Bean
  public JPAQueryFactory commonJpaQueryFactory(JpaConfig jpaConfig) {
    return jpaConfig.jpaQueryFactory();
  }
}
