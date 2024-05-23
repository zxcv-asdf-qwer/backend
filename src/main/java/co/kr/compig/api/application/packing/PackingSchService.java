package co.kr.compig.api.application.packing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.packing.PackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackingSchService {

	private final PackingRepository packingRepository;
	// private final WalletService walletService;
	//
	// public void transactionWallet(TransactionType transactionType, ExchangeType exchangeType, String description) {
	// 	List<Packing> packings = packingRepository.findByEndDateTimeLessThanEqualAndOrderStatusAndApplyStatusAndWalletIsNull(
	// 		LocalDateTime.now(), OrderStatus.MATCHING_COMPLETE, ApplyStatus.MATCHING_COMPLETE);
	// 	packings.forEach(packing -> walletService.createWallet(packing, transactionType, exchangeType, description));
	// }

}
