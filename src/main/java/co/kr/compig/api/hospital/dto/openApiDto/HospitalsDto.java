package co.kr.compig.api.hospital.dto.openApiDto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "body")
public class HospitalsDto {
	private List<HospitalDto> items;
	private Long totalCount;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	public List<HospitalDto> getItems() {
		return items;
	}

	public void setItems(List<HospitalDto> items) {
		this.items = items;
	}

	@XmlElement(name = "totalCount")
	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
