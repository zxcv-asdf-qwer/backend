package co.kr.compig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;


@SpringBootTest
public class TestControllerTest {

  @MockBean
  private JwtDecoder jwtDecoder;
  @MockBean
  private ClientRegistrationRepository clientRegistrationRepository;

  @Test
  public void test() {

  }
}
