package co.kr.compig.api.domain.code;

public enum IsYn {
	Y, N;

	public static IsYn of(final String isYn) {
		if (isYn.equals("Y")) {
			return IsYn.Y;
		}

		return IsYn.N;
	}
}
