package clay.vehicle.vehicles;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a vehicle in the system. This class contains all the properties of a vehicle and
 * implements validation constraints to ensure data integrity. It also implements Comparable for
 * sorting vehicles by engine power.
 */
@Getter
@Setter
@ToString
public class Vehicle implements Comparable<Vehicle> {
  /** Unique identifier of the vehicle. Must be positive and unique. */
  @Setter @Positive
  private int id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,

  // Значение этого поля должно генерироваться автоматически

  /** Name of the vehicle. Cannot be null or empty. */
  @NotNull @NotEmpty private String name; // Поле не может быть null, Строка не может быть пустой

  /** Coordinates of the vehicle's location. Cannot be null. */
  @JsonUnwrapped @NotNull private Coordinates coordinates;

  /** Date and time when the vehicle was created. Cannot be null. */
  @NotNull @Setter private java.time.ZonedDateTime creationDate;

  /** Power of the vehicle's engine. Must be positive. Not null. */
  @NotNull @Positive private Float enginePower;

  /** Total distance travelled by the vehicle. Must be positive. Not null. */
  @NotNull @Positive private Float distanceTravelled;

  /** Type of the vehicle. Can be null. */
  private VehicleType type;

  /** Type of fuel used by the vehicle. Cannot be null. */
  @NotNull private FuelType fuelType;

  /** User ID of the vehicle. Cannot be null. */
  @NotNull private int userId;

  /**
   * Constructs a new Vehicle with the specified attributes.
   *
   * @param id the unique identifier of the vehicle
   * @param name the name of the vehicle
   * @param coordinates the coordinates of the vehicle's location
   * @param creationDate the date and time when the vehicle was created
   * @param enginePower the power of the vehicle's engine
   * @param distanceTravelled the total distance travelled by the vehicle
   * @param type the type of the vehicle
   * @param fuelType the type of fuel used by the vehicle
   * @param userId the user ID of the vehicle
   */
  public Vehicle(
      int id,
      String name,
      Coordinates coordinates,
      ZonedDateTime creationDate,
      Float enginePower,
      Float distanceTravelled,
      VehicleType type,
      FuelType fuelType,
      int userId) {
    this.id = id;
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.enginePower = enginePower;
    this.distanceTravelled = distanceTravelled;
    this.type = type;
    this.fuelType = fuelType;
    this.userId = userId;
  }

  /**
   * Compares this vehicle with another vehicle based on engine power. Returns a negative integer,
   * zero, or a positive integer as this vehicle's engine power is less than, equal to, or greater
   * than the other vehicle's.
   *
   * @param v the vehicle to be compared
   * @return a negative integer, zero, or a positive integer as this vehicle's engine power is less
   *     than, equal to, or greater than the other vehicle's
   * @throws NullPointerException if the specified vehicle is null
   */
  public int compareTo(Vehicle v) {
    return (int) ((this.enginePower - v.getEnginePower()) * 1000);
  }
}
