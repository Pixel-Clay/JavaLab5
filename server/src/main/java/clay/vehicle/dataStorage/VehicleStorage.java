package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a storage container for vehicle data. This class provides functionality to store,
 * retrieve, and manage vehicle objects using a HashMap with vehicle IDs as keys. It also maintains
 * an initialization date for the storage and supports serialization.
 */
public class VehicleStorage implements Storage, Comparable<VehicleStorage> {

  private final DbStoreManager db;
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  /** The initialization date of this storage */
  @Getter @Setter private ZonedDateTime initDate;

  /** The storage map containing vehicles indexed by their IDs */
  @Getter @Setter private Map<Integer, Vehicle> storage = new HashMap<>();

  /** Constructs a new VehicleStorage with the current date and time as initialization date. */
  public VehicleStorage(DbStoreManager db) {
    this.initDate = ZonedDateTime.now();
    this.db = db;
  }

  /**
   * Inserts a vehicle into the storage.
   *
   * @param vehicle the vehicle to insert
   */
  public void insert(Vehicle vehicle) throws SQLException {
    lock.writeLock().lock();
    try {
      db.insert(vehicle);
      this.storage.put(vehicle.getId(), vehicle);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Gets all vehicles in the storage as a list.
   *
   * @return a list containing all vehicles in the storage
   */
  public Collection<Vehicle> getValues() {
    lock.readLock().lock();
    try {
      return new ArrayList<>(this.storage.values());
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets a vehicle by its ID.
   *
   * @param id the ID of the vehicle to retrieve
   * @return the vehicle with the specified ID, or null if not found
   */
  public Vehicle getElement(int id) {
    lock.readLock().lock();
    try {
      return this.storage.get(id);
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Removes a vehicle from the storage by its ID.
   *
   * @param id the ID of the vehicle to remove
   * @return the removed vehicle, or null if not found
   */
  public Vehicle removeKey(int id) throws SQLException {
    lock.writeLock().lock();
    try {
      db.removeKey(id);
      return this.storage.remove(id);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Updates a vehicle in the storage.
   *
   * @param id the ID of the vehicle to update
   * @param vehicle the new vehicle data
   */
  public void updateElement(int id, Vehicle vehicle) throws SQLException {
    lock.writeLock().lock();
    try {
      db.update(id, vehicle);
      this.storage.put(id, vehicle);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Gets the type name of the storage implementation.
   *
   * @return the class name of the storage implementation
   */
  public String getType() {
    lock.readLock().lock();
    try {
      return storage.getClass().getName();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets the number of vehicles in the storage.
   *
   * @return the size of the storage
   */
  public Integer getLen() {
    lock.readLock().lock();
    try {
      return storage.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets all vehicle IDs in the storage.
   *
   * @return a set of all vehicle IDs
   */
  public Set<Integer> getKeys() {
    lock.readLock().lock();
    try {
      return new HashSet<>(storage.keySet());
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets the entire storage collection.
   *
   * @return the HashMap containing all vehicles
   */
  public Map<Integer, Vehicle> getCollection() {
    lock.readLock().lock();
    try {
      return storage;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets the next available ID for a new vehicle. If the storage is empty, returns 1. Otherwise,
   * returns the maximum ID + 1.
   *
   * @return the next available ID
   */
  public int getNextId() throws SQLException {
    lock.readLock().lock();
    try {
      return db.nextVehicleId();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Clears all vehicles from the storage. This method creates a new empty HashMap to replace the
   * current storage.
   */
  public void clearCollection() throws SQLException {
    lock.writeLock().lock();
    try {
      db.truncateVehicles();
      db.resetVehicleIDs();
      this.storage = new HashMap<>();
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void clearLocalCollection() {
    lock.writeLock().lock();
    try {
      this.storage = new HashMap<>();
    } finally {
      lock.writeLock().unlock();
    }
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
    lock.readLock().lock();
    o.lock.readLock().lock();
    try {
      if (storage.keySet().equals(o.getKeys())) {
        for (int key : storage.keySet()) {
          if (storage.get(key) != o.getElement(key)) {
            return -9999;
          }
        }
        return 0;
      } else return o.getLen() - this.storage.size();
    } finally {
      o.lock.readLock().unlock();
      lock.readLock().unlock();
    }
  }
}
