package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Storage {
  void insert(Vehicle vehicle) throws SQLException;

  Collection<Vehicle> getValues();

  Vehicle getElement(int id);

  Vehicle removeKey(int id) throws SQLException;

  void updateElement(int id, Vehicle vehicle) throws SQLException;

  String getType();

  Integer getLen();

  Set<Integer> getKeys();

  Map<Integer, Vehicle> getCollection();

  int getNextId() throws SQLException;

  void clearCollection() throws SQLException;
}
