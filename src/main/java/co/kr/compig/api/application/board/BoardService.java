package co.kr.compig.api.application.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import co.kr.compig.api.domain.board.Board;
import co.kr.compig.api.domain.board.BoardRepository;
import co.kr.compig.api.domain.board.BoardRepositoryCustom;
import co.kr.compig.api.domain.file.SystemFile;
import co.kr.compig.api.domain.file.SystemFileRepository;
import co.kr.compig.api.presentation.board.request.BoardCreateRequest;
import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.request.BoardUpdateRequest;
import co.kr.compig.api.presentation.board.response.BoardDetailResponse;
import co.kr.compig.api.presentation.board.response.BoardResponse;
import co.kr.compig.api.presentation.board.response.SystemFileResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.S3Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

	private final BoardRepository boardRepository;
	private final BoardRepositoryCustom boardRepositoryCustom;
	private final SystemFileRepository systemFileRepository;
	private final S3Util s3Util;

	public Long createBoard(BoardCreateRequest boardCreateRequest,
		MultipartHttpServletRequest multipartRequest) {
		MultiValueMap<String, MultipartFile> mvm = multipartRequest.getMultiFileMap();
		Set<String> multiFileKeys = mvm.keySet();
		List<MultipartFile> multipartFiles = new ArrayList<>();
		for (String multiFileKey : multiFileKeys) {//파일 객체 갯수 for문
			for (MultipartFile multipartFile : mvm.get(multiFileKey)) {
				multipartFiles.add(multipartFile);
			}
		}
		List<String> imageUrlList = s3Util.uploads(multipartFiles);
		boardCreateRequest.setImageUrlListAndThumbnail(imageUrlList, 0);
		boardCreateRequest.setImageUrlList(imageUrlList);
		Board board = boardCreateRequest.converterEntity();
		return boardRepository.save(board).getId();
	}

	@Transactional(readOnly = true)
	public Page<BoardResponse> pageListBoard(BoardSearchRequest boardSearchRequest,
		Pageable pageable) {
		return boardRepositoryCustom.findPage(boardSearchRequest, pageable);
	}

	public Long updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest) {
		Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
		board.update(boardUpdateRequest);
		return board.getId();
	}

	public void deleteBoard(Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
		boardRepository.delete(board);
	}

	@Transactional(readOnly = true)
	public BoardDetailResponse getBoard(Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
		board.increaseViewCount();
		return board.toBoardDetailResponse();
	}

	public Long createBoardBaseFile(BoardCreateRequest boardCreateRequest,
		Map<String, String> files) {
		Board board = new Board();
		if (files != null) {
			List<SystemFileResponse> imageUrlList = s3Util.uploadBase64ToFile(files);
			boardCreateRequest.setThumbnailImageUrl(imageUrlList, 0);
			board = boardCreateRequest.converterEntity();
			boardRepository.save(board);
			saveSystemFile(imageUrlList, board.getId());
		} else {
			board = boardCreateRequest.converterEntity();
			boardRepository.save(board);
		}

		return board.getId();
	}

	private void saveSystemFile(List<SystemFileResponse> systemFileResponses, Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
		for (SystemFileResponse systemFileResponse : systemFileResponses) {
			SystemFile systemFile = SystemFile.builder()
				.filePath(systemFileResponse.getFilePath())
				.fileNm(systemFileResponse.getFileNm())
				.fileExtension(systemFileResponse.getFileExtension())
				.board(board)
				.build();
			systemFileRepository.save(systemFile);
		}
	}

	@Transactional(readOnly = true)
	public Slice<BoardResponse> pageListBoardCursor(@Valid BoardSearchRequest boardSearchRequest,
		Pageable pageable) {
		return boardRepositoryCustom.findAllByCondition(boardSearchRequest, pageable);
	}
}
