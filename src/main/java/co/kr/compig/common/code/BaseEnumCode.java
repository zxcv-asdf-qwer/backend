package co.kr.compig.common.code;

public interface BaseEnumCode<T> {
    T getCode();
    String getDesc();

    /**
     * Enum 데이터를 desc(code) 형태의 스트링으로 리턴
     */
    default String convertString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getDesc());
        builder.append("(");
        builder.append(getCode());
        builder.append(")");

        return builder.toString();
    }
}
