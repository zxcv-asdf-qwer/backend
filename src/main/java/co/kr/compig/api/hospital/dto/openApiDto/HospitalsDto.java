package co.kr.compig.api.hospital.dto.openApiDto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "body")
public class HospitalsDto {
    private List<HospitalDto> items;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public List<HospitalDto> getItems() {
        return items;
    }

    public void setItems(List<HospitalDto> items) {
        this.items = items;
    }
}
