package clay.vehicle;

import clay.vehicle.vehicles.Vehicle;
import java.util.*;
import lombok.Getter;

public class VehicleStorage {
  @Getter private HashMap<Integer, Vehicle> storage = new HashMap<>();

  @Getter private ArrayList<Integer> license_plates = new ArrayList<>();

  public void insert(Vehicle vehicle) {
    this.storage.put(license_plates.get(license_plates.size() - 1), vehicle);
  }

  public Vehicle getElement(int id) {
    return this.storage.get(id);
  }

  public void setElement(int id, Vehicle vehicle) {
    this.storage.put(id, vehicle);
  }
}
