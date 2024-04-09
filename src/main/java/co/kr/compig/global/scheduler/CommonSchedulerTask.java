package co.kr.compig.global.scheduler;

import org.springframework.context.annotation.Profile;

import co.kr.compig.api.application.hospital.HospitalSchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile({"dev", "prod"})
@Slf4j
// @Component
@RequiredArgsConstructor
public class CommonSchedulerTask {
	private final HospitalSchService hospitalSchService;

	// @Scheduled(cron = "0 3 * * 1#1")
	// @SchedulerLock(name = "HospitalService_insertHospitalInfo", lockAtLeastFor = "PT5M", lockAtMostFor = "PT10M")
	// public void insertHospitalInfo() throws Exception {
	// 	hospitalSchService.insertAllHospital();
	// }
}
