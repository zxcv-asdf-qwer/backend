package co.kr.compig.api.domain.push;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DeviceRepository extends JpaRepository<Device, Long>, QuerydslPredicateExecutor<Push> {
	Optional<Device> findByDeviceUuid(String deviceUuid);

	List<Device> findAllByMember_Id(String userId);
}
