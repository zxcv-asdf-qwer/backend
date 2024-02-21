package co.kr.compig;

import static co.kr.compig.common.util.SecurityUtil.getCustomOauth2User;

import co.kr.compig.common.security.CustomOauth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping
  public String testController() {
    CustomOauth2User customOauth2User = getCustomOauth2User();
    return "ok?";
  }
}
