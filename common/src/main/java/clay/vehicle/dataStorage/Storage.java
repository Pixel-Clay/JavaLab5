package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Storage {
  void insert(Vehicle vehicle);

  Collection<Vehicle> getValues();

  Vehicle getElement(int id);

  Vehicle removeKey(int id);

  void updateElement(int id, Vehicle vehicle);

  String getType();

  Integer getLen();

  Set<Integer> getKeys();

  Map<Integer, Vehicle> getCollection();

  int getNextId();

  void clearCollection();
}
