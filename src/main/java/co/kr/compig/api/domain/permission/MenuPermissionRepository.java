package co.kr.compig.api.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuPermissionRepository extends JpaRepository<MenuPermission, Long> {

	//    void deleteAllByGroupKey(String groupKey);

	//    void deleteAllByMember(Member member);
}
