package co.kr.compig.service.hospital;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
  private Item items ;

  public Body() {
  }

  public Item getItems() {
    return items;
  }

  public void setItems(Item items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "Body{" +
        " items='" + items + '\'' +
        '}';
  }
}
