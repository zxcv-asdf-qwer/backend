package co.kr.compig.api.application.packing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.wallet.WalletService;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackingSchService {

	private final PackingRepository packingRepository;
	private final WalletService walletService;

	public void transactionWallet(TransactionType transactionType, ExchangeType exchangeType, String description) {
		List<Packing> packings = packingRepository.findByEndDateTimeLessThanEqualAndOrderStatusAndApplyStatusAndWalletIsNull(
			LocalDateTime.now(), OrderStatus.MATCHING_COMPLETE, ApplyStatus.MATCHING_COMPLETE);
		packings.forEach(packing -> walletService.createWallet(packing, transactionType, exchangeType, description));
	}

}
