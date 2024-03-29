package co.kr.compig.api.domain.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;

@Repository
public interface WalletRepositoryCustom {
	Page<WalletResponse> findPage(WalletSearchRequest walletSearchRequest, Pageable pageable);
}
