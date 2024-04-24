package co.kr.compig.api.domain.packing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingRepository extends JpaRepository<Packing, Long>,
	PackingRepositoryCustom {
}
