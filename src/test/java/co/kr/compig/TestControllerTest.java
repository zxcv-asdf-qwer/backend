package co.kr.compig;

import co.kr.compig.config.TestConfig;
import co.kr.compig.global.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TestControllerTest {

    @Test
    public void test() {

    }
}
