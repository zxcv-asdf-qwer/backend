package co.kr.compig.global.utils;

import static co.kr.compig.global.code.PeriodType.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

import org.apache.commons.lang3.StringUtils;

import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.global.error.exception.BizException;

public class CalculateUtil {
	public static int calculateAgeFromJumin(String jumin) {
		if (StringUtils.isEmpty(jumin)) {
			return 0;
		}
		// 주민등록번호에서 생년월일 추출
		String birthYearStr = jumin.substring(0, 2); // 생년(YY)
		String birthMonthStr = jumin.substring(2, 4); // 생월
		String birthDayStr = jumin.substring(4, 6); // 생일

		// 현재 년도 가져오기
		int currentYear = LocalDate.now().getYear();

		// 생년월일을 현재 년도 기준으로 만 나이로 계산
		int birthYear = Integer.parseInt(birthYearStr);
		int birthMonth = Integer.parseInt(birthMonthStr);
		int birthDay = Integer.parseInt(birthDayStr);

		LocalDate birthDate = LocalDate.of(currentYear - 100 + birthYear, birthMonth, birthDay);

		Period age = Period.between(birthDate, LocalDate.now());
		return age.getYears();
	}

	public static Integer calculateYearsFromStartYear(Integer startYear) {
		if (startYear == null) {
			return null;
		}
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();

		return currentYear - startYear;
	}

	//지불해야 할 금액 계산(간병일 하루)
	public static Integer calculatePaymentPriceOneDay(CareOrderCalculateRequest careOrderCalculateRequest,
		Integer guardianFees) {
		if (careOrderCalculateRequest.getAmount() == null) {
			throw new BizException("금액을 입력해주세요.");
		}
		if (careOrderCalculateRequest.getPeriodType() == PART_TIME) {
			// 두 날짜와 시간 사이의 차이 계산
			Duration duration = Duration.between(careOrderCalculateRequest.getStartDateTime(),
				careOrderCalculateRequest.getEndDateTime());
			// 차이를 시간 단위로 변환
			long hours = duration.toHours();
			// 금액 * 시간
			long result = careOrderCalculateRequest.getAmount().longValue() * hours;

			// 100에서 수수료를 더한 뒤, 100으로 나누어 실제 곱해야 할 비율을 계산합니다.
			// discountRate가 Integer이므로, 100.0과 같이 실수로 나누어 자동 형변환을 유도합니다.
			double multiplier = (100.0 + guardianFees) / 100.0;

			// 이제 result에 multiplier를 곱하여 보호자 수수료를 적용한 값을 구합니다.
			long paymentResult = (long)(result * multiplier);

			return (int)paymentResult;
		}
		if (careOrderCalculateRequest.getPeriodType() == PERIOD) {
			// 100에서 수수료를 더한 뒤, 100으로 나누어 실제 곱해야 할 비율을 계산합니다.
			// discountRate가 Integer이므로, 100.0과 같이 실수로 나누어 자동 형변환을 유도합니다.
			double multiplier = (100.0 + guardianFees) / 100.0;

			// 이제 result에 multiplier를 곱하여 보호자 수수료를 적용한 값을 구합니다.
			long paymentResult = (long)(careOrderCalculateRequest.getAmount() * multiplier);

			return (int)paymentResult;
		}
		throw new BizException("계산 될 수 없습니다.");
	}
}
