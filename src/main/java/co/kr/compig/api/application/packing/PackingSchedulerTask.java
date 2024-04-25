package co.kr.compig.api.application.packing;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.TransactionType;
import co.kr.compig.global.scheduler.AbstractScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile({"dev", "prod"})
@Slf4j
@Component
@RequiredArgsConstructor
public class PackingSchedulerTask extends AbstractScheduler {
	private final PackingSchService packingSchService;
	//lockAtMostFor - 잠금이 최대로 유지되는 시간
	//lockAtLeastFor - 잠금이 최소로 유지되는 시간
	@Scheduled(cron = "* 0/30 * * * *")   // 매일 30분 마다 실행
	@SchedulerLock(name = "PackingSchService_transactionWallet", lockAtLeastFor = "PT5M", lockAtMostFor = "PT10M")
	public void transactionWallet() {
		setupToken();
		packingSchService.transactionWallet(TransactionType.CREDIT, ExchangeType.AUTO, "");
	}
}
