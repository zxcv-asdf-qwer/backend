package co.kr.compig.api.application.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
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
	private final MemberRepository memberRepository;
	private final PackingRepository packingRepository;

	public Long createWallet(WalletCreateRequest walletCreateRequest) {
		Member member = memberRepository.findById(walletCreateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		Packing packing = packingRepository.findById(walletCreateRequest.getPackingId())
			.orElseThrow(NotExistDataException::new);
		Wallet wallet = walletCreateRequest.converterEntity(member, packing);
		return walletRepository.save(wallet).getId();
	}

	@Transactional(readOnly = true)
	public Page<WalletResponse> pageListWallet(WalletSearchRequest walletSearchRequest, Pageable pageable) {
		return walletRepositoryCustom.findPage(walletSearchRequest, pageable);
	}

	public WalletDetailResponse getWallet(Long walletId) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		return wallet.toWalletDetailResponse();
	}

	public Long updateWallet(Long walletId, WalletUpdateRequest walletUpdateRequest) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		Member member = memberRepository.findById(walletUpdateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		Packing packing = packingRepository.findById(walletUpdateRequest.getPackingId())
			.orElseThrow(NotExistDataException::new);

		wallet.update(member, packing);
		return wallet.getId();
	}

	public void deleteWallet(Long walletId) {
		Wallet wallet = walletRepository.findById(walletId).orElseThrow(NotExistDataException::new);
		walletRepository.delete(wallet);
	}
}
