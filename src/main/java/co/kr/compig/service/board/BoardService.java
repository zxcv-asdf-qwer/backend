package co.kr.compig.service.board;

import co.kr.compig.api.board.dto.BoardCreateRequest;
import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.api.board.dto.FileResponse;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.common.util.S3Util;
import co.kr.compig.domain.board.Board;
import co.kr.compig.domain.board.BoardRepository;
import co.kr.compig.domain.board.BoardRepositoryCustom;
import co.kr.compig.domain.file.SystemFile;
import co.kr.compig.domain.file.SystemFileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardRepositoryCustom boardRepositoryCustom;
  private final SystemFileRepository systemFileRepository;
  private final S3Util s3Util;

  public Long createBoard(BoardCreateRequest boardCreateRequest, MultipartHttpServletRequest multipartRequest) {
    MultiValueMap<String, MultipartFile> mvm = multipartRequest.getMultiFileMap();
    Set<String> multiFileKeys = mvm.keySet();
    List<MultipartFile> multipartFiles = new ArrayList<>();
    for (String multiFileKey : multiFileKeys) {//파일 객체 갯수 for문
      for(MultipartFile multipartFile : mvm.get(multiFileKey)){
        multipartFiles.add(multipartFile);
      }
    }
    List<String> imageUrlList = s3Util.uploads(multipartFiles);
    boardCreateRequest.setImageUrlListAndThumbnail(imageUrlList, 0);
    boardCreateRequest.setImageUrlList(imageUrlList);
    Board board = boardCreateRequest.converterEntity();
    return boardRepository.save(board).getId();
  }


  public Page<BoardResponse> pageListBoard(BoardSearchRequest boardSearchRequest,
      Pageable pageable) {
    return boardRepositoryCustom.findPage(boardSearchRequest, pageable);
  }

  @Transactional
  public Long updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    board.update(boardUpdateRequest);
    return board.getId();
  }

  public Long deleteBoard(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    boardRepository.delete(board);
    return board.getId();
  }

  public BoardResponse getBoard(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    board.increaseViewCount();
    return new BoardResponse(board);
  }

  public Long createBoardBase(BoardCreateRequest boardCreateRequest,  Map<String, String> file) {
    List<String> imageUrlList = s3Util.uploadBase64(file);
    boardCreateRequest.setImageUrlListAndThumbnail(imageUrlList, 0);
    boardCreateRequest.setImageUrlList(imageUrlList);
    Board board = boardCreateRequest.converterEntity();
    return boardRepository.save(board).getId();
  }

  public Long createBoardBaseFile(BoardCreateRequest boardCreateRequest,  Map<String, String> file) {
    List<FileResponse> imageUrlList = s3Util.uploadBase64ToFile(file);
    Board board = boardCreateRequest.converterEntity();
    boardRepository.save(board);
    saveSystemFile(imageUrlList, board.getId());
    return board.getId();
  }

  private void saveSystemFile(List<FileResponse> fileResponses, Long boardId){
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    for(FileResponse fileResponse : fileResponses){
      SystemFile systemFile = SystemFile.builder()
          .s3Path(fileResponse.getS3Path())
          .fileNm(fileResponse.getFileNm())
          .fileExtension(fileResponse.getFileExtension())
          .board(board)
          .build();
      systemFileRepository.save(systemFile);
    }
  }
}
