package co.kr.compig.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 설정 클래스
 */
@EnableJpaAuditing // JPA Auditing 활성화
public class JpaConfig {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * JpaQueryFactory 빈 등록
   *
   * @return JPAQueryFactory 쿼리 및 DML 절 생성을 위한 팩토리 클래스
   */
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

}
