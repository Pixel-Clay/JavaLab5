package clay.vehicle.vehicles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coordinates {
  private double x; // Значение поля должно быть больше -731
  private double y; // Максимальное значение поля: 803

  public void setXY(double x, double y) {
    this.x = x;
    this.y = y;
  }
}
