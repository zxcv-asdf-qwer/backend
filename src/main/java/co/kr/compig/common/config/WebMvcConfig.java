package co.kr.compig.common.config;

import co.kr.compig.common.interceptor.ApiLogInterceptor;
import co.kr.compig.common.service.ApiLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  private final ObjectMapper objectMapper;
  private final ApiLogService apiLogService;

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    converters.add(new StringHttpMessageConverter());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new ApiLogInterceptor(apiLogService))
        .excludePathPatterns(
            "/",
            "/error",
            "/favicon.ico"
        );
  }
}
