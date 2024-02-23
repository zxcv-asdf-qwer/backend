package co.kr.compig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class TestControllerTest {

  @MockBean
  private JwtDecoder jwtDecoder;
  @MockBean
  private ClientRegistrationRepository clientRegistrationRepository;

  @Test
  public void test() {

  }
}
