package co.kr.compig.api.domain.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MenuRepository extends JpaRepository<Menu, Long>,
	QuerydslPredicateExecutor<Menu> {
	List<Menu> findAllByParent(Menu menu);

}
