package co.kr.compig.common.scheduler;

import co.kr.compig.service.hospital.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Profile({"dev", "prod"})
@Slf4j
@Component
@RequiredArgsConstructor
public class CommonSchedulerTask {
  private final HospitalService hospitalService;

  @Scheduled(cron = "0 3 * * 1#1")
  @SchedulerLock(name = "HospitalService_insertHospitalInfo", lockAtLeastFor = "PT5M", lockAtMostFor = "PT10M")
  public void insertHospitalInfo() throws Exception{
    hospitalService.insertAllHospital();
  }
}
