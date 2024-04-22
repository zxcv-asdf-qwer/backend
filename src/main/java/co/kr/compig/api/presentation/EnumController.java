package co.kr.compig.api.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "enum return", description = "enum 관련 API")
@RestController
public class EnumController {

	@Operation(summary = "조회")
	@GetMapping("/pb/enum-values/{enumName}")
	public ResponseEntity<?> getEnumValues(
		@Parameter(description = "enum Type class name") @PathVariable String enumName) {
		try {
			Class<?> enumClass = Class.forName("co.kr.compig.global.code." + enumName);
			if (enumClass.isEnum()) {
				return ResponseEntity.ok(enumClass.getMethod("values").invoke(null));
			} else {
				throw new NotExistDataException();
			}
		} catch (ClassNotFoundException e) {
			throw new NotExistDataException();
		} catch (Exception e) {
			throw new BizException("############### 서버 에러 ###############" + enumName);
		}
	}
}
