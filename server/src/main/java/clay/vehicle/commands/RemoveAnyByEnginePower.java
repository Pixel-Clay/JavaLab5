package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Command implementation for removing a vehicle with a specific engine power. This command removes
 * one vehicle from the storage that has the specified engine power. If multiple vehicles have the
 * same engine power, only one is removed.
 */
public class RemoveAnyByEnginePower implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new RemoveAnyByEnginePower command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveAnyByEnginePower(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the remove_any_by_engine_power command. Removes one vehicle that has the specified
   * engine power.
   *
   * @param args command arguments, where args[0] is the engine power value
   * @return a message indicating whether a vehicle was removed
   */
  @Override
  public String execute(String[] args) {
    if (args.length == 0) return "! Not enough arguments";

    float enginePower;
    try {
      enginePower = Float.parseFloat(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid argument";
    }

    Optional<Integer> lowerIds =
        this.storage.getCollection().values().stream()
            .filter(vehicle -> vehicle.getEnginePower().equals(enginePower))
            .filter(vehicle -> vehicle.getUserId() == Integer.parseInt(args[args.length - 1]))
            .map(Vehicle::getId)
            .findAny();

    if (lowerIds.isEmpty()) return "Removed 0 items";
    else {
      try {
        storage.removeKey(Integer.parseInt(String.valueOf(lowerIds)));
        return "Removed 1 item";
      } catch (SQLException e) {
        return "! Database error: " + e.getMessage();
      }
    }
  }
}
