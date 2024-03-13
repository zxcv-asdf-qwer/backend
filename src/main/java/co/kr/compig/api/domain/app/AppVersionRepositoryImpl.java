package co.kr.compig.api.domain.app;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppVersionRepositoryImpl implements AppVersionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<AppVersion> findByRecentVersion() {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(QAppVersion.appVersion)
			.orderBy(QAppVersion.appVersion.lastVer.desc())
			.fetchFirst()
		);
	}
}
