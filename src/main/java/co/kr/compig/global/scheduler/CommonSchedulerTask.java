package co.kr.compig.global.scheduler;

import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile({"dev", "prod"})
@Slf4j
// @Component
@RequiredArgsConstructor
public class CommonSchedulerTask {

}
