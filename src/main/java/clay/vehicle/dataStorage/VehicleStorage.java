package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a storage container for vehicle data. This class provides functionality to store,
 * retrieve, and manage vehicle objects using a HashMap with vehicle IDs as keys. It also maintains
 * an initialization date for the storage and supports serialization.
 */
@JsonSerialize(using = VehicleStorageSerializer.class)
public class VehicleStorage implements Comparable<VehicleStorage> {

  /** The initialization date of this storage */
  @Getter @Setter private ZonedDateTime initDate;

  /** The storage map containing vehicles indexed by their IDs */
  @Getter @Setter private Map<Integer, Vehicle> storage = new HashMap<>();

  /** Constructs a new VehicleStorage with the current date and time as initialization date. */
  public VehicleStorage() {
    this.initDate = ZonedDateTime.now();
  }

  /**
   * Inserts a vehicle into the storage.
   *
   * @param vehicle the vehicle to insert
   */
  public void insert(Vehicle vehicle) {
    this.storage.put(vehicle.getId(), vehicle);
  }

  /**
   * Gets all vehicles in the storage as a list.
   *
   * @return a list containing all vehicles in the storage
   */
  public Collection<Vehicle> getValues() {
    return this.storage.values();
  }

  /**
   * Gets a vehicle by its ID.
   *
   * @param id the ID of the vehicle to retrieve
   * @return the vehicle with the specified ID, or null if not found
   */
  public Vehicle getElement(int id) {
    return this.storage.get(id);
  }

  /**
   * Removes a vehicle from the storage by its ID.
   *
   * @param id the ID of the vehicle to remove
   * @return the removed vehicle, or null if not found
   */
  public Vehicle removeKey(int id) {
    return this.storage.remove(id);
  }

  /**
   * Updates a vehicle in the storage.
   *
   * @param id the ID of the vehicle to update
   * @param vehicle the new vehicle data
   */
  public void updateElement(int id, Vehicle vehicle) {
    this.storage.put(id, vehicle);
  }

  /**
   * Gets the type name of the storage implementation.
   *
   * @return the class name of the storage implementation
   */
  public String getType() {
    return storage.getClass().getName();
  }

  /**
   * Gets the number of vehicles in the storage.
   *
   * @return the size of the storage
   */
  public Integer getLen() {
    return storage.size();
  }

  /**
   * Gets all vehicle IDs in the storage.
   *
   * @return a set of all vehicle IDs
   */
  public Set<Integer> getKeys() {
    return storage.keySet();
  }

  /**
   * Gets the entire storage collection.
   *
   * @return the HashMap containing all vehicles
   */
  public Map<Integer, Vehicle> getCollection() {
    return storage;
  }

  /**
   * Gets the next available ID for a new vehicle. If the storage is empty, returns 1. Otherwise,
   * returns the maximum ID + 1.
   *
   * @return the next available ID
   */
  public int getNextId() {
    int nextId;
    try {
      nextId = Collections.max(this.storage.keySet().stream().toList()) + 1;
    } catch (NoSuchElementException e) {
      nextId = 1;
    }
    return nextId;
  }

  /**
   * Clears all vehicles from the storage. This method creates a new empty HashMap to replace the
   * current storage.
   */
  public void clearCollection() {
    this.storage = new HashMap<>();
  }

  /**
   * Compares this storage with another storage for order. The comparison is based on the number of
   * vehicles and their contents.
   *
   * @param o the storage to be compared
   * @return a negative integer, zero, or a positive integer as this storage is less than, equal to,
   *     or greater than the specified storage
   * @throws NullPointerException if the specified storage is null
   * @throws ClassCastException if the specified object's type prevents it from being compared
   */
  @Override
  public int compareTo(VehicleStorage o) {
    if (storage.keySet().equals(o.getKeys())) {
      for (int key : storage.keySet()) {
        if (storage.get(key) != o.getElement(key)) {
          return -9999;
        }
      }
      return 0;
    } else return o.getLen() - this.storage.size();
  }
}
