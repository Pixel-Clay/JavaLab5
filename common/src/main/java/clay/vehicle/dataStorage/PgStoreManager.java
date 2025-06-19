package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Coordinates;
import clay.vehicle.vehicles.FuelType;
import clay.vehicle.vehicles.Vehicle;
import clay.vehicle.vehicles.VehicleType;
import jakarta.validation.*;
import java.sql.*;
import java.time.ZoneId;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading vehicle data from CSV files. This class provides functionality to parse
 * CSV files containing vehicle information and convert them into VehicleStorage objects. It handles
 * validation of the data and ensures data consistency.
 */

/* vehicle table:
* id integer not null unique,
  name text not null,
  x double not null,
  y double not null,
  creation_date timestamp with time zone,
  engine_power numeric not null,
  distance_travelled double precision not null,
  type text,
  fuel_type text not null,
  user_id integer not null
*/
public class PgStoreManager {
  private static final Logger logger = LogManager.getLogger(PgStoreManager.class);

  private Connection connection;

  public void connect(String jdbcUrl, Properties properties) throws SQLException {
    connection = DriverManager.getConnection(jdbcUrl, properties);
    logger.info("Connected to " + jdbcUrl);
  }

  public void disconnect() throws SQLException {
    connection.close();
    logger.info("Disconnected from db");
  }

  public void syncFromDB(VehicleStorage storage) throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet dbVehicles = statement.executeQuery("select * from vehicles");

    int violationCounter = 0;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    storage.clearLocalCollection();

    Map<Integer, Vehicle> storageMap = storage.getCollection();

    while (dbVehicles.next()) {
      VehicleType vt;
      try {
        vt = VehicleType.valueOf(dbVehicles.getString("type"));
      } catch (NullPointerException e) {
        vt = null;
      }

      Vehicle vehicle =
          new Vehicle(
              dbVehicles.getInt("id"),
              dbVehicles.getString("name"),
              new Coordinates(dbVehicles.getDouble("x"), dbVehicles.getDouble("y")),
              dbVehicles.getTimestamp("creation_date").toInstant().atZone(ZoneId.systemDefault()),
              dbVehicles.getFloat("engine_power"),
              dbVehicles.getFloat("distance_travelled"),
              vt,
              FuelType.valueOf(dbVehicles.getString("fuel_type")),
              dbVehicles.getInt("user_id"));

      Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);
      if (!violations.isEmpty()) {
        violationCounter += 1;
        continue;
      }
      storageMap.put(vehicle.getId(), vehicle);
    }
    logger.info(
        "Downloaded "
            + storage.getLen()
            + " vehicles from db with "
            + violationCounter
            + " elements failing validation");
    statement.close();
  }

  public void insert(Vehicle vehicle) throws SQLException {
    Statement statement = connection.createStatement();
    PreparedStatement ps =
        connection.prepareStatement("insert into vehicles values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    String vt;
    if (vehicle.getType() == null) vt = null;
    else vt = vehicle.getType().toString();

    ps.setInt(1, vehicle.getId());
    ps.setString(2, vehicle.getName());
    ps.setDouble(3, vehicle.getCoordinates().getX());
    ps.setDouble(4, vehicle.getCoordinates().getY());
    ps.setTimestamp(5, Timestamp.from(vehicle.getCreationDate().toInstant()));
    ps.setFloat(6, vehicle.getEnginePower());
    ps.setFloat(7, vehicle.getDistanceTravelled());
    ps.setString(8, vt);
    ps.setString(9, vehicle.getFuelType().toString());
    ps.setInt(10, vehicle.getUserId());
    ps.executeUpdate();
    statement.close();
  }

  public int nextVehicleId() throws SQLException {
    String sql = "SELECT nextval('veh_id')";
    try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        throw new SQLException("Failed to retrieve next vehicle id from sequence.");
      }
    }
  }

  public void removeKey(int id) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement("DELETE FROM vehicles WHERE id = ?")) {
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new SQLException("Failed to delete vehicle with id: " + id, e);
    }
  }

  public void update(int id, Vehicle vehicle) throws SQLException {
    try (PreparedStatement ps =
        connection.prepareStatement(
            "UPDATE vehicles SET name = ?, x = ?, y = ?, creation_date = ?, engine_power = ?, distance_travelled = ?, type = ?, fuel_type = ?, user_id = ? WHERE id = ?")) {
      ps.setString(1, vehicle.getName());
      ps.setDouble(2, vehicle.getCoordinates().getX());
      ps.setDouble(3, vehicle.getCoordinates().getY());
      ps.setTimestamp(4, Timestamp.from(vehicle.getCreationDate().toInstant()));
      ps.setFloat(5, vehicle.getEnginePower());
      ps.setFloat(6, vehicle.getDistanceTravelled());
      ps.setString(7, vehicle.getType().toString());
      ps.setString(8, vehicle.getFuelType().toString());
      ps.setInt(9, vehicle.getUserId());
      ps.setInt(10, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new SQLException("Failed to update vehicle with id: " + id, e);
    }
  }

  public void truncateVehicles() throws SQLException {
    try (Statement stmt = connection.createStatement()) {
      stmt.executeUpdate("TRUNCATE TABLE vehicles");
    } catch (SQLException e) {
      logger.error("Failed to truncate vehicles table: " + e.getMessage());
      throw new SQLException("Failed to truncate vehicles table", e);
    }

    logger.info("Truncated table vehicles");
  }

  public void resetVehicleIDs() throws SQLException {
    try (Statement stmt = connection.createStatement()) {
      stmt.executeUpdate("ALTER SEQUENCE veh_id RESTART WITH 1");
    } catch (SQLException e) {
      logger.error("Failed to reset veh_id sequence: " + e.getMessage());
      throw new SQLException("Failed to reset veh_id sequence", e);
    }

    logger.info("Reset vehicle ids");
  }
}
