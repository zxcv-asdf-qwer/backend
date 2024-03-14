package co.kr.compig.api.domain.app;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.domain.code.AppOsType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppVersionRepositoryImpl implements AppVersionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<AppVersion> findRecentByOsCode(AppOsType osType) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(QAppVersion.appVersion)
			.where(QAppVersion.appVersion.osCode.eq(osType))
			.orderBy(QAppVersion.appVersion.createdAndModified.createdOn.desc())
			.fetchFirst()
		);
	}
}
