package co.kr.compig.service.hospital;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.kr.compig.api.hospital.dto.HospitalCreateRequest;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalDto;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalExceptionDto;
import co.kr.compig.api.hospital.dto.openApiDto.HospitalResponseVO;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.domain.hospital.Hospital;
import co.kr.compig.domain.hospital.HospitalRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HospitalSchService {

	private final HospitalRepository hospitalRepository;

	@Value("${api.hospital.url}")
	private String OPEN_API_URL;
	@Value("${api.hospital.service-key}")
	private String SERVICE_KEY;
	private final int PAGE_SIZE = 100;

	public Long countHospitals() throws Exception {
		Long totalCount = 0L;

		URI uri = createUri(1, 2);
		ResponseEntity<String> responseEntity = createResponseEntity(uri);

		String xmlResponseBody = responseEntity.getBody();
		if (StringUtils.hasText(xmlResponseBody)) {
			var hospitalResponse = unmarshal(HospitalResponseVO.class, xmlResponseBody);
			totalCount = hospitalResponse.getBody().getTotalCount();
			totalCount =
				totalCount % PAGE_SIZE != 0 ? totalCount / PAGE_SIZE + 1 : totalCount / PAGE_SIZE;
		}

		return totalCount;
	}

	public List<HospitalCreateRequest> getCreateHospitalList() throws Exception {
		List<HospitalCreateRequest> createRequests = new ArrayList<>();
		Long totalCount = countHospitals();
		hospitalRepository.deleteAll();

		for (int pageNo = 1; pageNo <= totalCount; pageNo++) {
			URI uri = createUri(pageNo, PAGE_SIZE);
			ResponseEntity<String> responseEntity = createResponseEntity(uri);

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
					}
					createRequests = createHospitalCreateRequests(hospitalResponse);

				} else {
					throw new BizException(
						String.format("Hospital - XmlResponseBody: %s 병원정보 가져오기 실패.", xmlResponseBody)
					);
				}
			}
		}

		return createRequests;
	}

	private List<HospitalCreateRequest> createHospitalCreateRequests(
		HospitalResponseVO hospitalResponse) {
		List<HospitalCreateRequest> createRequests = new ArrayList<>();
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
		return createRequests;
	}

	private URI createUri(int pageNo, int numOfRows) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(OPEN_API_URL);
		builder.queryParam("serviceKey", SERVICE_KEY);
		builder.queryParam("pageNo", pageNo);
		builder.queryParam("numOfRows", numOfRows);

		return builder.build(true).toUri();
	}

	private ResponseEntity<String> createResponseEntity(URI uri) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		// Accept 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

		// HttpEntity 생성
		HttpEntity<String> entity = new HttpEntity<>(headers);

		restTemplate.getMessageConverters()
			.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity,
			String.class);

		if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			throw new Exception(
				String.format("%s - 병원 정보 가져오기 실패 : %s",
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
					String.format("invalid.response " + responseEntity.getStatusCode()))
			);
		}

		return responseEntity;
	}

	public void insertAllHospital() throws Exception {
		List<HospitalCreateRequest> createRequests = getCreateHospitalList();
		for (HospitalCreateRequest hospitalCreateRequest : createRequests) {
			Hospital hospital = hospitalCreateRequest.converterEntity();
			hospitalRepository.save(hospital);
		}
		log.info("############################ 병원 데이터 저장 완료 ############################");
	}

	private String makeOperationHours(HospitalDto item) {
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
		return (T)unmarshaller.unmarshal(new StringReader(xml));
	}

	private String getTagName(String xml) {
		int startTagIndex = xml.indexOf("<") + 1;
		int endTagIndex = xml.indexOf(">");
		String tagName = xml.substring(startTagIndex, endTagIndex);
		if (!tagName.equals("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?")) {
			return tagName;
		} else {
			String newXml = xml.substring(endTagIndex + 1);
			return newXml.substring(newXml.indexOf("<") + 1, newXml.indexOf(">"));
		}
	}

	public String getXmlRootElementAnnoName(Class<?> clazz) {
		Annotation annotation = clazz.getAnnotation(XmlRootElement.class);
		if (annotation != null && annotation instanceof XmlRootElement rootElement) {
			return rootElement.name();
		} else {
			return null;
		}
	}
}

