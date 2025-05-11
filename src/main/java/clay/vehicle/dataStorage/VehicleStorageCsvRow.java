package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.FuelType;
import clay.vehicle.vehicles.Vehicle;
import clay.vehicle.vehicles.VehicleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class VehicleStorageCsvRow {
  @JsonProperty("license_plate")
  private final int licensePlate;

  // Vehicle fields flattened directly
  @JsonProperty("id")
  private final int vehicleId;

  @JsonProperty("name")
  private final String vehicleName;

  @JsonProperty("x")
  private final double coordX;

  @JsonProperty("y")
  private final double coordY;

  @JsonProperty("creationDate")
  private final ZonedDateTime creationDate;

  @JsonProperty("enginePower")
  private final float enginePower;

  @JsonProperty("distanceTravelled")
  private final float distanceTravelled;

  @JsonProperty("type")
  private final VehicleType vehicleType;

  @JsonProperty("fuelType")
  private final FuelType fuelType;

  @JsonProperty("init_date")
  private final ZonedDateTime initDate;

  public VehicleStorageCsvRow(int licensePlate, Vehicle vehicle, ZonedDateTime initDate) {
    this.licensePlate = licensePlate;
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

  // Add getters for all fields (required by Jackson)
  public int getLicensePlate() {
    return licensePlate;
  }

  public int getVehicleId() {
    return vehicleId;
  }

  public String getVehicleName() {
    return vehicleName;
  }

  public double getCoordX() {
    return coordX;
  }

  public double getCoordY() {
    return coordY;
  }

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public float getEnginePower() {
    return enginePower;
  }

  public float getDistanceTravelled() {
    return distanceTravelled;
  }

  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public FuelType getFuelType() {
    return fuelType;
  }

  public ZonedDateTime getInitDate() {
    return initDate;
  }
}
