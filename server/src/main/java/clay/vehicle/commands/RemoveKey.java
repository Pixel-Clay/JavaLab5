package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.sql.SQLException;

/**
 * Command implementation for removing a vehicle by its key. This command removes a vehicle from the
 * storage using its key (ID).
 */
public class RemoveKey implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new RemoveKey command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveKey(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the remove_key command. Removes a vehicle from the storage using the provided key.
   *
   * @param args command arguments, where args[0] is the key of the vehicle to remove
   * @return a message indicating the result of the removal operation
   */
  @Override
  public String execute(String[] args) {
    int key;
    if (args.length == 0) return "! Not enough arguments";
    else key = Integer.parseInt(args[0]);

    if (storage.getElement(key).getUserId() != Integer.parseInt(args[args.length - 1])) {
      return "! Permission error";
    }

    try {
      Vehicle res = storage.removeKey(key);
      if (res == null) return "! Specified key not found";
      else return "Removed key " + key;
    } catch (SQLException e) {
      return "! Database error: " + e.getMessage();
    }
  }
}
