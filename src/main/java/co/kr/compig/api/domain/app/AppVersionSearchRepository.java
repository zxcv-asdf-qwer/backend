package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AppVersionSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Optional<AppVersion> findByRecentVersion() {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(QAppVersion.appVersion)
			.orderBy(QAppVersion.appVersion.lastVer.desc())
			.fetchFirst()
		);
	}
}
