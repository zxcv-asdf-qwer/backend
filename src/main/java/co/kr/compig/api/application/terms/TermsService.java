package co.kr.compig.api.application.terms;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.terms.Terms;
import co.kr.compig.api.domain.terms.TermsRepository;
import co.kr.compig.api.domain.terms.TermsRepositoryCustom;
import co.kr.compig.api.presentation.board.request.TermsUpdateRequest;
import co.kr.compig.api.presentation.terms.request.TermsCreateRequest;
import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsDetailResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.dto.pagination.SliceResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TermsService {

	private final TermsRepository termsRepository;
	private final TermsRepositoryCustom termsRepositoryCustom;

	public Long createTermsAdmin(TermsCreateRequest adminTermsCreateRequest) {
		Terms terms = adminTermsCreateRequest.converterEntity();
		return termsRepository.save(terms).getId();
	}

	@Transactional(readOnly = true)
	public Page<TermsResponse> getTermsPage(TermsSearchRequest termsSearchRequest) {
		return termsRepositoryCustom.getTermsPage(termsSearchRequest);
	}

	@Transactional(readOnly = true)
	public TermsDetailResponse getTerms(Long termsId) {
		Terms terms = termsRepository.findById(termsId).orElseThrow(NotExistDataException::new);
		return terms.toTermsDetailResponse();
	}

	public Long updateTerms(Long termsId, TermsUpdateRequest termsUpdateRequest) {
		Terms terms = termsRepository.findById(termsId).orElseThrow(NotExistDataException::new);
		terms.update(termsUpdateRequest);
		return terms.getId();
	}

	public void deleteTerms(Long termsId) {
		Terms terms = termsRepository.findById(termsId).orElseThrow(NotExistDataException::new);
		termsRepository.delete(terms);
	}

	@Transactional(readOnly = true)
	public SliceResponse<TermsResponse> getTermsSlice(TermsSearchRequest termsSearchRequest, Pageable pageable) {
		Slice<TermsResponse> slice = termsRepositoryCustom.getTermsSlice(termsSearchRequest, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
	}

	@Transactional(readOnly = true)
	public List<TermsResponse> getTermsList(TermsSearchRequest termsSearchRequest) {
		return termsRepositoryCustom.getTermsList(termsSearchRequest);
	}
}
