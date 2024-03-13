package co.kr.compig.global;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import co.kr.compig.api.domain.code.BaseEnumCode;
import jakarta.servlet.ServletException;

@ExtendWith({
	SpringExtension.class,
	RestDocumentationExtension.class,
	MockitoExtension.class
})
@WebMvcTest(
	excludeAutoConfiguration = SecurityAutoConfiguration.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebSecurity.class)
)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class AbstractTest {

	protected MockMvc mvc;
	@Autowired
	protected ObjectMapper objectMapper;
	protected RestDocumentationResultHandler restDocs;
	private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(
		"yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) throws ServletException {
		this.restDocs = document(
			"{class-name}/{method-name}",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())
		);
		//        DelegatingFilterProxy delegateProxyFilter = new DelegatingFilterProxy();
		//        delegateProxyFilter.init(new MockFilterConfig(context.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));

		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(restDocumentation)
				//                        .uris()
				//                        .withScheme("https")
				//                        .withHost("dev.com/back")
				//                        .withPort(58090)
				//                        .and()
				//                        .snippets()
				//                        .withEncoding(StandardCharsets.UTF_8.displayName())
			)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.alwaysDo(this.restDocs)
			.build();
	}

	protected final String json(Object value) throws Exception {
		return objectMapper().writeValueAsString(value);
	}

	protected final MultiValueMap<String, String> queryParams(Object dto) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		Map<String, Object> dataMap = objectMapper().convertValue(dto, new TypeReference<>() {
		});
		dataMap.entrySet().stream()
			.filter(entry -> ObjectUtils.isNotEmpty(entry.getValue()))
			.forEach(
				entry -> {
					String key = entry.getKey();
					Object value = entry.getValue();

					if (value instanceof List) {
						((List<?>)value).forEach(v -> params.add(key, String.valueOf(v)));
					} else {
						params.add(key, String.valueOf(value));
					}
				});

		return params;
	}

	private ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
			.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
			.visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
			.serializers(
				new LocalDateSerializer(DATE_FORMATTER),
				new LocalDateTimeSerializer(DATETIME_FORMATTER),
				new StdSerializer<>(Enum.class) {
					@Override
					public void serialize(Enum value, JsonGenerator gen, SerializerProvider provider)
						throws IOException {
						gen.writeObject(
							value instanceof BaseEnumCode
								? ((BaseEnumCode<?>)value).getCode()
								: value.name());
					}
				})
			.build();
	}
}
