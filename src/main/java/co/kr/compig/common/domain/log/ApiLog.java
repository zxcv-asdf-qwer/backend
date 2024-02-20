package co.kr.compig.common.domain.log;

import co.kr.compig.common.dto.BaseAudit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 로그인 로그 엔티티
 */
@Getter
@NoArgsConstructor
@Entity
@SuperBuilder(toBuilder = true)
public class ApiLog extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    private Long siteId;

    private String userId;

    @Column(length = 10)
    private String httpMethod;

    @Column(length = 500)
    private String requestUrl;

    @Column(name = "ip_addr", length = 100)
    private String remoteIp;
}
