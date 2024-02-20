package co.kr.compig.common.config.jackson;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

public class CustomObjectMapper {

    public static ObjectMapper getObjectMapper() {
        Jackson2ObjectMapperBuilder builder = builder();
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        builder.serializers(new LocalDateTimeSerializer(DATETIME_FORMATTER));
        builder.deserializers(new LocalDateTimeDeserializer(DATETIME_FORMATTER));

        return builder.build();
    }

    private static Jackson2ObjectMapperBuilder builder() {
        return Jackson2ObjectMapperBuilder
                .json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .modules(new JavaTimeModule(), new Jdk8Module());
    }

}
