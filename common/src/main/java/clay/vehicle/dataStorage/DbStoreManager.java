package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;
import java.util.Properties;

public interface DbStoreManager {
  void connect(String jdbcUrl, Properties properties) throws SQLException;

  void disconnect() throws SQLException;

  void syncFromDB(Storage storage) throws SQLException;

  void insert(Vehicle vehicle) throws SQLException;

  int nextVehicleId() throws SQLException;

  void removeKey(int id) throws SQLException;

  void update(int id, Vehicle vehicle) throws SQLException;

  void truncateVehicles() throws SQLException;

  void resetVehicleIDs() throws SQLException;

  void resetUsers() throws SQLException;

  void createUser(String login, String password) throws SQLException;

  Integer verifyLogin(String login, String password) throws SQLException;
}
