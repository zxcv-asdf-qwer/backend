package co.kr.compig.api.domain.code.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.api.domain.code.DiseaseCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiseaseCodeListConverter implements AttributeConverter<List<DiseaseCode>, String> {
	protected String getEnumName() {
		return "DiseaseCode";
	}

	protected DiseaseCode[] getValueList() {
		return DiseaseCode.values();
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<DiseaseCode> attribute) {
		try {
			return objectMapper.writeValueAsString(attribute.stream()
				.map(DiseaseCode::getCode).toList());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DiseaseCode> convertToEntityAttribute(String dbData) {
		try {
			List<String> diseaseCodes = objectMapper.readValue(dbData, new TypeReference<>() {
			});
			return diseaseCodes.stream()
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
