package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.FuelType;
import clay.vehicle.vehicles.Vehicle;
import clay.vehicle.vehicles.VehicleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.NoArgsConstructor;

/**
 * Represents a row of vehicle data in CSV format. This class is used for serializing and
 * deserializing vehicle data to/from CSV files. It flattens the vehicle data structure for CSV
 * storage, including coordinates and initialization date.
 */
@NoArgsConstructor
public class VehicleStorageCsvRow {

  /** Unique identifier of the vehicle */
  @JsonProperty("id")
  private int vehicleId;

  /** Name of the vehicle */
  @JsonProperty("name")
  private String vehicleName;

  /** X coordinate of the vehicle's location */
  @JsonProperty("x")
  private double coordX;

  /** Y coordinate of the vehicle's location */
  @JsonProperty("y")
  private double coordY;

  /** Date and time when the vehicle was created */
  @JsonProperty("creationDate")
  private ZonedDateTime creationDate;

  /** Power of the vehicle's engine */
  @JsonProperty("enginePower")
  private float enginePower;

  /** Total distance travelled by the vehicle */
  @JsonProperty("distanceTravelled")
  private float distanceTravelled;

  /** Type of the vehicle */
  @JsonProperty("type")
  private VehicleType vehicleType;

  /** Type of fuel used by the vehicle */
  @JsonProperty("fuelType")
  private FuelType fuelType;

  /** Date and time when the VehicleStorage collection was initialized */
  @JsonProperty("init_date")
  private ZonedDateTime initDate;

  /**
   * Constructs a new VehicleStorageCsvRow from a Vehicle object and initialization date.
   *
   * @param vehicle the vehicle to convert to CSV row format
   * @param initDate the initialization date of the VehicleStorage collection
   */
  public VehicleStorageCsvRow(Vehicle vehicle, ZonedDateTime initDate) {
    this.vehicleId = vehicle.getId();
    this.vehicleName = vehicle.getName();
    this.coordX = vehicle.getCoordinates().getX();
    this.coordY = vehicle.getCoordinates().getY();
    this.creationDate = vehicle.getCreationDate();
    this.enginePower = vehicle.getEnginePower();
    this.distanceTravelled = vehicle.getDistanceTravelled();
    this.vehicleType = vehicle.getType();
    this.fuelType = vehicle.getFuelType();
    this.initDate = initDate;
  }

  /**
   * Gets the vehicle's unique identifier.
   *
   * @return the vehicle ID
   */
  public int getVehicleId() {
    return vehicleId;
  }

  /**
   * Gets the vehicle's name.
   *
   * @return the vehicle name
   */
  public String getVehicleName() {
    return vehicleName;
  }

  /**
   * Gets the X coordinate of the vehicle's location.
   *
   * @return the X coordinate
   */
  public double getCoordX() {
    return coordX;
  }

  /**
   * Gets the Y coordinate of the vehicle's location.
   *
   * @return the Y coordinate
   */
  public double getCoordY() {
    return coordY;
  }

  /**
   * Gets the vehicle's creation date and time.
   *
   * @return the creation date and time
   */
  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  /**
   * Gets the vehicle's engine power.
   *
   * @return the engine power
   */
  public float getEnginePower() {
    return enginePower;
  }

  /**
   * Gets the total distance travelled by the vehicle.
   *
   * @return the distance travelled
   */
  public float getDistanceTravelled() {
    return distanceTravelled;
  }

  /**
   * Gets the type of the vehicle.
   *
   * @return the vehicle type
   */
  public VehicleType getVehicleType() {
    return vehicleType;
  }

  /**
   * Gets the type of fuel used by the vehicle.
   *
   * @return the fuel type
   */
  public FuelType getFuelType() {
    return fuelType;
  }

  /**
   * Gets the initialization date and time of the VehicleStorage collection.
   *
   * @return the initialization date and time
   */
  public ZonedDateTime getInitDate() {
    return initDate;
  }
}
