package co.kr.compig.service.hospital;

import co.kr.compig.api.hospital.dto.*;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalDto;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalExceptionDto;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalResponseVO;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.hospital.Hospital;
import co.kr.compig.domain.hospital.HospitalRepository;
import co.kr.compig.domain.hospital.HospitalRepositoryCustom;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final HospitalRepositoryCustom hospitalRepositoryCustom;

  private String OPEN_API_URL = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncFullDown";
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

  public Long createHospital(HospitalCreateRequest hospitalCreateRequest) {
    Hospital hospital = hospitalCreateRequest.converterEntity();
    return hospitalRepository.save(hospital).getId();
  }

  public Long countHospitals(){
    Long totalCount = 0L;
    try {
      RestTemplate restTemplate = new RestTemplate();

      // Accept 헤더 설정
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

      // HttpEntity 생성
      HttpEntity<String> entity = new HttpEntity<>(headers);

      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(OPEN_API_URL);
      builder.queryParam("serviceKey", SERVICE_KEY);
      builder.queryParam("pageNo", 1);
      builder.queryParam("numOfRows", 2);

      URI uri = builder.build(true).toUri();

      restTemplate.getMessageConverters()
          .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
      ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity,
          String.class);

      if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
        throw new Exception(
            String.format("%s - 병원 정보 가져오기 실패 : %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                String.format("invalid.response " + responseEntity.getStatusCode()))
        );
      }

      String xmlResponseBody = responseEntity.getBody();
      if (StringUtils.hasText(xmlResponseBody)) {
        String tagName = getTagName(xmlResponseBody);

        if (getXmlRootElementAnnoName(HospitalExceptionDto.class).equals(tagName)) {
          var hospitalErrorResponse = unmarshal(HospitalExceptionDto.class, xmlResponseBody);
          throw new BizException(String.format("Hospital Open API 응답 %s 로 인해 실패",
              hospitalErrorResponse.getCmmMsgHeader().getErrMsg()));
        } else if (getXmlRootElementAnnoName(HospitalResponseVO.class).equals(tagName)) {
          var hospitalResponse = unmarshal(HospitalResponseVO.class, xmlResponseBody);
          if (!StringUtils.pathEquals(hospitalResponse.getHeader().getResultCode(), "00")) {
            throw new BizException(String.format("Hospital Open API 응답 %s 로 인해 실패",
                hospitalResponse.getHeader().getResultCode()));
          } else {
            totalCount = hospitalResponse.getBody().getTotalCount();
          }
        }else{
          throw new BizException(
              String.format("Hospital - XmlResponseBody: %s 병원정보 가져오기 실패.", xmlResponseBody)
          );
        }
      }
    }catch (Exception e){
      log.error("{}", e.getMessage());
    }
    totalCount = totalCount % PAGE_SIZE != 0? totalCount / PAGE_SIZE + 1 : totalCount / PAGE_SIZE;
    return totalCount;
  }

  public List<HospitalCreateRequest> getCreateHospitalList() {
    List<HospitalCreateRequest> createRequests = new ArrayList<>();
    Long totalCount = countHospitals();
    try {
      RestTemplate restTemplate = new RestTemplate();

      // Accept 헤더 설정
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

      // HttpEntity 생성
      HttpEntity<String> entity = new HttpEntity<>(headers);

      for(int pageNo = 1; pageNo <= totalCount ; pageNo++){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(OPEN_API_URL);
        builder.queryParam("serviceKey", SERVICE_KEY);
        builder.queryParam("pageNo", pageNo);
        builder.queryParam("numOfRows", PAGE_SIZE);

        URI uri = builder.build(true).toUri();

        restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity,
            String.class);

        if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
          throw new Exception(
              String.format("%s - 병원 정보 가져오기 실패 : %s",
                  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                  String.format("invalid.response " + responseEntity.getStatusCode()))
          );
        }

        String xmlResponseBody = responseEntity.getBody();
        if (StringUtils.hasText(xmlResponseBody)) {
          String tagName = getTagName(xmlResponseBody);

          if (getXmlRootElementAnnoName(HospitalExceptionDto.class).equals(tagName)) {
            var hospitalErrorResponse = unmarshal(HospitalExceptionDto.class, xmlResponseBody);
            throw new BizException(String.format("Hospital Open API 응답 %s 로 인해 실패",
                hospitalErrorResponse.getCmmMsgHeader().getErrMsg()));
          } else if (getXmlRootElementAnnoName(HospitalResponseVO.class).equals(tagName)) {
            var hospitalResponse = unmarshal(HospitalResponseVO.class, xmlResponseBody);
            if (!StringUtils.pathEquals(hospitalResponse.getHeader().getResultCode(), "00")) {
              throw new BizException(String.format("Hospital Open API 응답 %s 로 인해 실패",
                  hospitalResponse.getHeader().getResultCode()));
            } else {
              for (HospitalDto item : hospitalResponse.getBody().getItems()) {
                HospitalCreateRequest hospitalCreateRequest = HospitalCreateRequest.builder()
                    .hospitalNm(item.getDutyName())
                    .hospitalCode(item.getPostCdn1() + item.getPostCdn2())
                    .hospitalAddress(item.getDutyAddr())
                    .hospitalTelNo(item.getDutyTel1())
                    .hospitalOperationHours(makeOperationHours(item))
                    .build();
                createRequests.add(hospitalCreateRequest);
              }
            }
          }else{
            throw new BizException(
                String.format("Hospital - XmlResponseBody: %s 병원정보 가져오기 실패.", xmlResponseBody)
            );
          }
        }
      }
    }catch (Exception e){
      log.error("{}", e.getMessage());
    }
    return createRequests;
  }

  public String insertAllHospital(){
    List<HospitalCreateRequest> createRequests = getCreateHospitalList();
    for (HospitalCreateRequest hospitalCreateRequest : createRequests) {
      Hospital hospital = hospitalCreateRequest.converterEntity();
      hospitalRepository.save(hospital);
    }
    return "병원 데이터 저장 완료";
  }

  private String makeOperationHours(HospitalDto item){
    return String.format("월 %s~%s\n화 %s~%s\n수 %s~%s\n목 %s~%s\n금 %s~%s\n",
            item.getDutyTime1s(), item.getDutyTime1c(),
            item.getDutyTime2s(), item.getDutyTime2c(),
            item.getDutyTime3s(), item.getDutyTime3c(),
            item.getDutyTime4s(), item.getDutyTime4c(),
            item.getDutyTime5s(), item.getDutyTime5c());
  }
  public <T> T unmarshal(Class<T> clazz, String xml) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    return (T) unmarshaller.unmarshal(new StringReader(xml));
  }

  private String getTagName(String xml){
    int startTagIndex = xml.indexOf("<") + 1;
    int endTagIndex = xml.indexOf(">");
    String tagName = xml.substring(startTagIndex, endTagIndex);
    if(!tagName.equals("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?")){
      return tagName;
    }else{
      String newXml = xml.substring(endTagIndex+1);
      return newXml.substring(newXml.indexOf("<")+1, newXml.indexOf(">"));
    }
  }

  public String getXmlRootElementAnnoName(Class<?> clazz){
    Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
    if(annotation != null && annotation instanceof XmlRootElement){
      XmlRootElement rootElement = (XmlRootElement) annotation;
      return rootElement.name();
    }else{
      return null;
    }
  }

}

