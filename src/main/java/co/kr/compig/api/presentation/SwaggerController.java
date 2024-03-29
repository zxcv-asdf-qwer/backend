package co.kr.compig.api.presentation;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class SwaggerController {
	@RequestMapping(path = "/login")
	public void doSocialLogin(HttpServletResponse reponse) throws IOException {
		reponse.sendRedirect("/swagger-ui/index.html");
	}

}
