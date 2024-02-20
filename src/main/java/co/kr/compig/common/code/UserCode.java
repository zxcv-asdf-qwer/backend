package co.kr.compig.common.code;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserCode implements BaseEnumCode<String> {

    // length = 3
    SYSTEM("SYS", "user.type.sys"), // 시스템사용자 (System)
    INTERNAL("INT", "user.type.int"), // 내부사용자 (Internal)
    EXTERNAL("EXT", "user.type.ext") // 외부사용자 (External)
    ;

    private final String code;
    private final String desc;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
