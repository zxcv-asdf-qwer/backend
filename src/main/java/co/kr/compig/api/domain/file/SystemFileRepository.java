package co.kr.compig.api.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SystemFileRepository extends JpaRepository<SystemFile, String>,
	QuerydslPredicateExecutor<SystemFile> {

}
