package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize(using = VehicleStorageSerializer.class)
public class VehicleStorage implements Comparable<VehicleStorage> {

  @Getter ZonedDateTime initDate;

  public VehicleStorage() {
    this.initDate = ZonedDateTime.now();
  }

  @Getter @Setter private HashMap<Integer, Vehicle> storage = new HashMap<>();

  @Getter private ArrayList<Integer> license_plates = new ArrayList<>();

  public void insert(Vehicle vehicle) {
    int plate;
    try {
      plate = license_plates.get(license_plates.size() - 1) + 1;
    } catch (IndexOutOfBoundsException e) {
      license_plates.add(1);
      plate = 1;
    }
    this.storage.put(plate, vehicle);
  }

  public Vehicle getElement(int id) {
    return this.storage.get(id);
  }

  public void setElement(int id, Vehicle vehicle) {
    this.storage.put(id, vehicle);
  }

  public String getType() {
    return storage.getClass().getName();
  }

  public Integer getLen() {
    return storage.size();
  }

  public Set<Integer> getKeys() {
    return storage.keySet();
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
   * @param o the object to be compared.
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
  @Override
  public int compareTo(VehicleStorage o) {
    return 0;
  }
}
