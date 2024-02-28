package co.kr.compig.api.social.dto;

import co.kr.compig.common.code.MemberRegisterType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SocialLoginRequest {
    @NotNull
    private MemberRegisterType memberRegisterType;
    @NotNull
    private String code;
}
