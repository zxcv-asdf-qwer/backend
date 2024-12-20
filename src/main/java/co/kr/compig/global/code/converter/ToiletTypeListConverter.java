package co.kr.compig.global.code.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.global.code.ToiletType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ToiletTypeListConverter implements AttributeConverter<List<ToiletType>, String> {
	protected String getEnumName() {
		return "ToiletType";
	}

	protected ToiletType[] getValueList() {
		return ToiletType.values();
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<ToiletType> attribute) {
		if (attribute == null) {
			return null; // null일 경우 그대로 반환
		}
		try {
			return objectMapper.writeValueAsString(attribute.stream()
				.map(ToiletType::getCode).toList());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ToiletType> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null; // null일 경우 그대로 반환
		}
		try {
			List<String> toiletCodes = objectMapper.readValue(dbData, new TypeReference<>() {
			});
			return toiletCodes.stream()
				.map(code -> code == null ? null : Arrays.stream(getValueList())
					.filter(e -> e.getCode().equals(code))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(
						String.format("Enum %s에 Code %s가 없습니다.", getEnumName(), code))))
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
