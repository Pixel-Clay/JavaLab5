package clay.vehicle.vehicles;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FuelType {
  ALCOHOL,
  MANPOWER,
  NUCLEAR;

  @JsonValue
  public String toValue() {
    return name();
  }
}
