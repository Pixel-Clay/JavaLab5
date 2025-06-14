package clay.vehicle.vehicles;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents geographical coordinates in the system. This class contains x and y coordinates with
 * validation constraints to ensure they are within valid ranges.
 */
@Getter
@Setter
@AllArgsConstructor
public class Coordinates {

  /** X coordinate. Must be greater than -731. */
  @NotNull
  @DecimalMin(value = "-731.0", message = "Значение поля должно быть больше -731")
  private double x;

  /** Y coordinate. Must be less than to 803. */
  @NotNull
  @DecimalMax(value = "803", message = "Максимальное значение поля: 803")
  private double y; // Максимальное значение поля: 803

  /**
   * Sets both x and y coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void setCoordinates(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Coordinates{" + "x=" + x + ", y=" + y + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordinates that = (Coordinates) o;
    return Double.compare(getX(), that.getX()) == 0 && Double.compare(getY(), that.getY()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY());
  }
}
