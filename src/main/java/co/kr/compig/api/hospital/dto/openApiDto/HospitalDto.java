package co.kr.compig.api.hospital.dto.openApiDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
public class HospitalDto {
  private Response response;

  @Getter
  @Setter
  public static class Response{
    private Header header;
    private Body body;
  }

  @Getter
  @Setter
  public static class Header{
    private String resultCode;
    private String resultMsg;
  }

  @Getter
  @Setter
  public static class Body{
    private Items items;
    private Long totalCount;
  }


  @Getter
  @Setter
  public static class Items{
    private List<Item> item;
  }


  @Getter
  @Setter
  public static class Item{
    private String dutyAddr;
    private String dutyDiv;
    private String dutyDivNam;
    private String dutyEmcls;
    private String dutyEmclsName;
    private int dutyEryn;
    private String dutyName;
    private String dutyTel1;
    private String dutyTime1c;
    private String dutyTime1s;
    private String dutyTime2c;
    private String dutyTime2s;
    private String dutyTime3c;
    private String dutyTime3s;
    private String dutyTime4c;
    private String dutyTime4s;
    private String dutyTime5c;
    private String dutyTime5s;
    private String hpid;
    private String postCdn1;
    private String postCdn2;
    private int rnum;
    private double wgs84Lat;
    private double wgs84Lon;
  }

}
