package co.kr.compig.api.application.info.push;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.push.Device;
import co.kr.compig.api.domain.push.DeviceRepository;
import co.kr.compig.api.presentation.push.request.DeviceCreate;
import co.kr.compig.api.presentation.push.request.DeviceUpdate;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceService {

	private final MemberService memberService;
	private final DeviceRepository deviceRepository;

	public Long save(DeviceCreate deviceCreate) {
		Optional<Device> optionalDevice = deviceRepository.findByDeviceUuid(deviceCreate.getDeviceUuid());

		if (optionalDevice.isPresent()) {
			Device device = optionalDevice.get();
			device.update(deviceCreate.getPhoneType(), deviceCreate.getPushKey(), deviceCreate.getModelName(),
				deviceCreate.getOsVersion(), deviceCreate.getIsAgreeReceive());
			return device.getId();
		}

		return deviceRepository.save(deviceCreate.converterEntity()).getId();
	}

	public Long update(String uuid, DeviceUpdate deviceUpdate) {
		Device device = detailByDeviceUuid(uuid);
		device.update(deviceUpdate.getPhoneType(), deviceUpdate.getPushKey(), deviceUpdate.getModelName(),
			deviceUpdate.getOsVersion(), deviceUpdate.getIsAgreeReceive());

		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		member.addDevices(device);

		return device.getId();
	}

	public void removeDeviceUser(String uuid) {
		Device detail = detail(uuid);
		deviceRepository.delete(detail);
	}

	@Transactional(readOnly = true)
	public Device detail(String uuid) {
		Set<Device> devices = new HashSet<>(memberService.getMemberById(SecurityUtil.getMemberId()).getDevices());

		if (CollectionUtils.isEmpty(devices)) {
			throw new BizException("사용자의 Device 정보가 존재 하지 않습니다.");
		}

		return devices.stream().filter(d -> d.getDeviceUuid().equals(uuid))
			.findFirst().orElseThrow(() -> new NotExistDataException(
				"존재 하지 않는 Device UUID 입니다. (" + uuid + ")"));
	}

	@Transactional(readOnly = true)
	public Device detailByDeviceUuid(String deviceUuid) {
		return deviceRepository.findByDeviceUuid(deviceUuid).orElseThrow(NotExistDataException::new);
	}

	@Transactional(readOnly = true)
	public List<String> uuidListByMemberId(String memberId) {
		return deviceRepository.findAllByMember_Id(memberId)
			.stream()
			.map(Device::getDeviceUuid)
			.collect(Collectors.toList());
	}

}
