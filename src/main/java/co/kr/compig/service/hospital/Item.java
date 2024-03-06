package co.kr.compig.service.hospital;

import co.kr.compig.api.hospital.dto.openApiDto.HospitalDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

  private List<HospitalDto> item ;

  public Item() {
  }


  public List<HospitalDto> getItem() {
    return item;
  }



  public void setItem(List<HospitalDto> item) {
    this.item = item;
  }

  @Override
  public String toString() {
    return "Item{" +
        " item='" + item.toString() + '\'' +
        '}';
  }

}