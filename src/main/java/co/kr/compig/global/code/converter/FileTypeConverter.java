package co.kr.compig.global.code.converter;

import co.kr.compig.global.code.FileType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FileTypeConverter extends AbstractBaseEnumConverter<FileType, String> {

	@Override
	protected String getEnumName() {
		return "FileType";
	}

	@Override
	protected FileType[] getValueList() {
		return FileType.values();
	}
}
