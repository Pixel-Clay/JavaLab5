package clay.vehicle.vehicles;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enumeration of possible vehicle types in the system. This enum represents different categories of
 * vehicles that can be stored and managed by the system.
 */
@Getter
public enum VehicleType {
  CAR,
  PLANE,
  HELICOPTER,
  HOVERBOARD,
  SPACESHIP;

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
