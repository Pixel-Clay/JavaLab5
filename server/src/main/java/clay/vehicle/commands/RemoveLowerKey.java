package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command implementation for removing vehicles with IDs less than a specified value. This command
 * removes all vehicles from the storage that have an ID less than the provided ID value.
 */
public class RemoveLowerKey implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new RemoveLowerKey command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveLowerKey(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the remove_lower_key command. Removes all vehicles with IDs less than the specified
   * ID.
   *
   * @param args command arguments, where args[0] is the ID threshold
   * @return a message indicating how many vehicles were removed
   */
  @Override
  public String execute(String[] args) {
    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid id";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    int counter = 0;

    Set<Integer> lowerIds =
        this.storage.getCollection().values().stream()
            .filter(vehicle -> vehicle.getUserId().equals(Integer.parseInt(args[args.length - 1])))
            .map(Vehicle::getId)
            .filter(vehicleId -> vehicleId < id)
            .collect(Collectors.toSet());

    try {
      for (Integer i : lowerIds) {
        storage.removeKey(i);
        counter++;
      }
    } catch (SQLException e) {
      return "! Database error: " + e.getMessage();
    }

    return "Removed " + counter + " items";
  }
}
