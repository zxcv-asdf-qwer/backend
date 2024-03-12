package co.kr.compig;

import static co.kr.compig.common.utils.SecurityUtil.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.common.security.CustomOauth2User;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping
	//  @PreAuthorize("hasAuthority('ROLE_user')")
	public String testController() {
		CustomOauth2User customOauth2User = getCustomOauth2User();
		return "ok?";
	}
}
