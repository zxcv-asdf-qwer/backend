package co.kr.compig.service.hospital;

import co.kr.compig.api.hospital.dto.HospitalCreateRequest;
import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import co.kr.compig.api.hospital.dto.HospitalUpdateRequest;
import co.kr.compig.api.hospital.dto.openApiDto.CmmMsgHeader;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalDto;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalExceptionDto;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import co.kr.compig.domain.hospital.Hospital;
import co.kr.compig.domain.hospital.HospitalRepository;
import co.kr.compig.domain.hospital.HospitalRepositoryCustom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final HospitalRepositoryCustom hospitalRepositoryCustom;

  private final String SERVICE_KEY = "IbT4eJVladZuxTws1cU4oeEiZvvLybEDfY0Unx%2BmZXbsFD8a18SqcaB7PRX%2B88QioAwm0DyJib0MgOtZAlsvTg%3D%3D";

  private final int PAGE_SIZE = 100;

  @Transactional(readOnly = true)
  public Page<HospitalResponse> pageListHospital(HospitalSearchRequest hospitalSearchRequest,
      Pageable pageable) {
    return hospitalRepositoryCustom.findPage(hospitalSearchRequest, pageable);
  }

  @Transactional(readOnly = true)
  public Slice<HospitalResponse> pageListHospitalCursor(HospitalSearchRequest hospitalSearchRequest,
      Pageable pageable) {
    return hospitalRepositoryCustom.findAllByCondition(hospitalSearchRequest, pageable);
  }

  @Transactional(readOnly = true)
  public HospitalDetailResponse getHospital(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(NotExistDataException::new);
    return hospital.toHospitalDetailResponse();
  }

  public Long updateHospital(Long hospitalId, HospitalUpdateRequest hospitalUpdateRequest) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(NotExistDataException::new);
    hospital.update(hospitalUpdateRequest);
    return hospital.getId();
  }

  public Long deleteHospital(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId)
        .orElseThrow(NotExistDataException::new);
    hospitalRepository.delete(hospital);
    return hospital.getId();
  }

  public String createAllHospital(){
    hospitalRepository.deleteAll();
    Long totalPageCnt = getTotalPageCnt();
    for(int i = 1; i <= totalPageCnt; i++){
     List<HospitalCreateRequest> pageHospitalCreateRequests = createHospitalRequest(i, 100);
      createHospital(pageHospitalCreateRequests);
    }
    return "병원정보 저장 완료";
  }
  public void createHospital(List<HospitalCreateRequest> hospitalCreateRequests) {
    for(HospitalCreateRequest hospitalCreateRequest : hospitalCreateRequests){
      Hospital hospital = hospitalCreateRequest.converterEntity();
      hospitalRepository.save(hospital);
    }
  }

  public List<HospitalCreateRequest> createHospitalRequest(int page, int numOfRows){
    HospitalDto hospitalDto = makeHospitalDto(page, numOfRows);
    List<HospitalDto.Item> items = hospitalDto.getResponse().getBody().getItems().getItem();
    List<HospitalCreateRequest> createRequests = new ArrayList<>();
    for(HospitalDto.Item item : items){
      HospitalCreateRequest hospitalCreateRequest = HospitalCreateRequest.builder()
          .hospitalNm(item.getDutyName())
          .hospitalCode(item.getPostCdn1()+item.getPostCdn2())
          .hospitalAddress(item.getDutyAddr())
          .hospitalTelNo(item.getDutyTel1())
          .hospitalOperationHours(makeOperationHours(item))
          .build();
      createRequests.add(hospitalCreateRequest);
    }
    return createRequests;
  }

  private String makeOperationHours(HospitalDto.Item item){
    return String.format("월 %s~%s\n화 %s~%s\n수 %s~%s\n목 %s~%s\n금 %s~%s\n",
        item.getDutyTime1s(), item.getDutyTime1c(),
        item.getDutyTime2s(), item.getDutyTime2c(),
        item.getDutyTime3s(), item.getDutyTime3c(),
        item.getDutyTime4s(), item.getDutyTime4c(),
        item.getDutyTime5s(), item.getDutyTime5c());
  }

  public Long getTotalPageCnt() {
    HospitalDto hospitalDto = makeHospitalDto(1, 2);
    Long totalCnt = hospitalDto.getResponse().getBody().getTotalCount();
    return totalCnt % PAGE_SIZE == 0 ? totalCnt / PAGE_SIZE : totalCnt / PAGE_SIZE + 1;
  }

  public HospitalDto makeHospitalDto(int pageNo, int numOfRows) {
    URI uri = makeURL(pageNo,numOfRows);

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

    try{
      String jsonResponse = restTemplate.getForObject(uri, String.class);

      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
          .create();

      return gson.fromJson(jsonResponse, HospitalDto.class);
    }catch (Exception e){
      ResponseEntity<HospitalExceptionDto> response = restTemplate.getForEntity(
          uri, HospitalExceptionDto.class);

      HospitalExceptionDto serviceResponse = response.getBody();
      CmmMsgHeader cmmMsgHeader = serviceResponse.getCmmMsgHeader();

      String errorMsg = cmmMsgHeader.getErrMsg();

      throw new IllegalStateException(errorMsg);
    }
  }


  public URI makeURL(int pageNo, int numOfRows){
    return UriComponentsBuilder.fromHttpUrl("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncFullDown")
        .queryParam("serviceKey", SERVICE_KEY)
        .queryParam("pageNo", pageNo)
        .queryParam("numOfRows", numOfRows)
        .build(true)
        .toUri();
  }
}

