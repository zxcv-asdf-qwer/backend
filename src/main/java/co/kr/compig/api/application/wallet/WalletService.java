package co.kr.compig.api.application.wallet;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.packing.PackingService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.domain.wallet.WalletRepository;
import co.kr.compig.api.domain.wallet.WalletRepositoryCustom;
import co.kr.compig.api.presentation.wallet.request.WalletCreateRequest;
import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.request.WalletUpdateRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

	private final WalletRepository walletRepository;
	private final WalletRepositoryCustom walletRepositoryCustom;
	private final MemberService memberService;
	private final PackingService packingService;

	public Long createWallet(WalletCreateRequest walletCreateRequest) {
		Member member = memberService.getMemberById(walletCreateRequest.getMemberId());
		Packing packing = packingService.getPackingById(walletCreateRequest.getPackingId());
		Wallet wallet = walletCreateRequest.converterEntity(member, packing);
		return walletRepository.save(wallet).getId();
	}

	@Transactional(readOnly = true)
	public Page<WalletResponse> getWalletPage(WalletSearchRequest walletSearchRequest) {
		return walletRepositoryCustom.findPage(walletSearchRequest);
	}

	@Transactional(readOnly = true)
	public WalletDetailResponse getWallet(Long walletId) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		return wallet.toWalletDetailResponse();
	}

	public Long updateWallet(Long walletId, WalletUpdateRequest walletUpdateRequest) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		Member member = memberService.getMemberById(walletUpdateRequest.getMemberId());
		Packing packing = packingService.getPackingById(walletUpdateRequest.getPackingId());
		wallet.update(member, packing);
		return wallet.getId();
	}

	public void deleteWallet(Long walletId) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		walletRepository.delete(wallet);
	}
}
