package co.kr.compig.global.utils;

import java.time.LocalDate;
import java.time.Period;

import org.apache.commons.lang3.StringUtils;

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
}
