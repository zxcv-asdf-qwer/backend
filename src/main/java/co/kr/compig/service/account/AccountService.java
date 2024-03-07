package co.kr.compig.service.account;

import co.kr.compig.api.account.dto.AccountCreateRequest;
import co.kr.compig.api.account.dto.AccountDetailResponse;
import co.kr.compig.api.account.dto.AccountUpdateRequest;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.account.Account;
import co.kr.compig.domain.account.AccountRepository;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

  private final AccountRepository accountRepository;
  private final MemberRepository memberRepository;

  public Long createAccount(AccountCreateRequest accountCreateRequest) {
    Member member = memberRepository.findById(accountCreateRequest.getMemberId()).orElseThrow(
        NotExistDataException::new);
    Account account = accountCreateRequest.converterEntity(member);
    accountRepository.save(account);
    return account.getId();
  }


  public AccountDetailResponse getAccount(Long accountId) {
    Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
    return account.toAccountDetailResponse();
  }

  public Long updateAccount(Long accountId, AccountUpdateRequest accountUpdateRequest) {
    Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
    account.update(accountUpdateRequest);
    return account.getId();
  }

  public Long deleteAccount(Long accountId) {
    Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
    accountRepository.delete(account);
    return account.getId();
  }
}
