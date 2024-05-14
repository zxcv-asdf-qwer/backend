package co.kr.compig.global.utils;

import static co.kr.compig.global.code.PeriodType.*;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.global.error.exception.BizException;

public class CalculateUtil {
	public static int calculateAgeFromJumin(String jumin1, String jumin2) {
		if (StringUtils.isEmpty(jumin1)) {
			return 0;
		}

		// 주민등록번호를 통해 입력 받은 날짜
		int year = Integer.parseInt(jumin1.substring(0, 2));
		int month = Integer.parseInt(jumin1.substring(2, 4));
		int day = Integer.parseInt(jumin1.substring(4, 6));

		// 오늘 날짜
		LocalDate today = LocalDate.now();
		int todayYear = today.getYear();
		int todayMonth = today.getMonthValue();
		int todayDay = today.getDayOfMonth();

		// 주민등록번호 뒷자리로 몇년대인지
		String gender = jumin2.substring(0, 1);
		if (gender.equals("1") || gender.equals("2")) {
			year += 1900;
		} else if (gender.equals("3") || gender.equals("4")) {
			year += 2000;
		} else if (gender.equals("0") || gender.equals("9")) {
			year += 1800;
		}

		// 올해 - 태어난년도
		int americanAge = todayYear - year;

		// 생일이 안지났으면 - 1
		if (month > todayMonth) {
			americanAge--;
		} else if (month == todayMonth) {
			if (day > todayDay) {
				americanAge--;
			}
		}

		return americanAge;
	}

	public static Integer calculateYearsFromStartYear(Integer startYear) {
		if (startYear == null || startYear == 0) {
			return null;
		}
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();

		return currentYear - startYear;
	}

	//지불해야 할 금액 계산(간병일 하루+보호자 수수료)
	public static Integer calculatePaymentPriceOneDay(CareOrderCalculateRequest careOrderCalculateRequest,
		Integer guardianFees) {
		if (careOrderCalculateRequest.getAmount() == null) {
			throw new BizException("금액을 입력해주세요.");
		}
		if (careOrderCalculateRequest.getPeriodType() == PART_TIME) {
			// 차이를 시간 단위로 변환
			long hours = careOrderCalculateRequest.getPartTime();
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

	//금액 계산(간병일 하루 금액만)
	public static Integer calculatePriceOneDay(CareOrderCalculateRequest careOrderCalculateRequest) {
		if (careOrderCalculateRequest.getAmount() == null) {
			throw new BizException("금액을 입력해주세요.");
		}
		if (careOrderCalculateRequest.getPeriodType() == PART_TIME) {
			// 차이를 시간 단위로 변환
			long hours = careOrderCalculateRequest.getPartTime();
			// 금액 * 시간
			long result = careOrderCalculateRequest.getAmount().longValue() * hours;

			return (int)result;
		}
		if (careOrderCalculateRequest.getPeriodType() == PERIOD) {
			return careOrderCalculateRequest.getAmount();
		}
		throw new BizException("계산 될 수 없습니다.");
	}
}
