package co.kr.compig.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

//    void deleteAllByGroupKey(String groupKey);

//    void deleteAllByMember(Member member);
}
