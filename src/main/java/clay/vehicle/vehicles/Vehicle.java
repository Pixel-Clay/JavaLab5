package clay.vehicle.vehicles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class Vehicle implements Comparable<Vehicle> {
  @Setter(AccessLevel.NONE)
  private int id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
  // Значение этого поля должно генерироваться автоматически
  private String name; // Поле не может быть null, Строка не может быть пустой
  private Coordinates coordinates; // Поле не может быть null
  private java.time.ZonedDateTime
      creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
  // автоматически
  private Float enginePower; // Поле не может быть null, Значение поля должно быть больше 0
  private Float distanceTravelled; // Поле не может быть null, Значение поля должно быть больше 0
  private VehicleType type; // Поле может быть null
  private FuelType fuelType; // Поле не может быть null

  public Vehicle(
      int id,
      String name,
      Coordinates coordinates,
      ZonedDateTime creationDate,
      Float enginePower,
      Float distanceTravelled,
      VehicleType type,
      FuelType fuelType) {
    this.id = id;
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.enginePower = enginePower;
    this.distanceTravelled = distanceTravelled;
    this.type = type;
    this.fuelType = fuelType;
  }

  /**
   * Compares this object with the specified object for order. Returns a negative integer, zero, or
   * a positive integer as this object is less than, equal to, or greater than the specified object.
   *
   * <p>The implementor must ensure {@link Integer#signum signum}{@code (x.compareTo(y)) ==
   * -signum(y.compareTo(x))} for all {@code x} and {@code y}. (This implies that {@code
   * x.compareTo(y)} must throw an exception if and only if {@code y.compareTo(x)} throws an
   * exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive: {@code (x.compareTo(y) > 0
   * && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
   *
   * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0} implies that {@code
   * signum(x.compareTo(z)) == signum(y.compareTo(z))}, for all {@code z}.
   *
   * @param v the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException if the specified object's type prevents it from being compared to
   *     this object.
   * @apiNote It is strongly recommended, but <i>not</i> strictly required that {@code
   *     (x.compareTo(y)==0) == (x.equals(y))}. Generally speaking, any class that implements the
   *     {@code Comparable} interface and violates this condition should clearly indicate this fact.
   *     The recommended language is "Note: this class has a natural ordering that is inconsistent
   *     with equals."
   */
  public int compareTo(Vehicle v) {
    return (int)
        ((v.getEnginePower() - v.getDistanceTravelled()) - (this.enginePower - this.distanceTravelled));
  }
}
