package co.kr.compig.domain.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountRepositoryImpl{
  private final JPAQueryFactory jpaQueryFactory;

}
