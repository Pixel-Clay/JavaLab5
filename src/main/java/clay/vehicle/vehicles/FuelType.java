package clay.vehicle.vehicles;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enumeration of possible fuel types in the system. This enum represents different types of fuel
 * that can be used by vehicles in the system.
 */
@Getter
public enum FuelType {
  ALCOHOL,
  MANPOWER,
  NUCLEAR;

  /**
   * Returns the name of this enum constant as a string. This method is used for serialization.
   *
   * @return the name of this enum constant
   */
  @JsonValue
  public String toValue() {
    return name();
  }
}
