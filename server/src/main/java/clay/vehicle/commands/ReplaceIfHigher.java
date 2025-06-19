package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import jakarta.validation.ValidationException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Command implementation for replacing a vehicle if the new one is greater. This command replaces
 * an existing vehicle with a new one only if the new vehicle is greater than the existing one.
 */
public class ReplaceIfHigher implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new ReplaceIfHigher command with the specified storage.
   *
   * @param storage the storage where vehicles will be replaced
   */
  public ReplaceIfHigher(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the replace_if_greater command. Takes a vehicle ID as argument and prompts for a new
   * vehicle. If the new vehicle is greater than the existing one, replaces it.
   *
   * @param args command arguments, where args[0] is the ID of the vehicle to potentially replace
   * @return a message indicating whether a replacement occurred
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
    Vehicle replace;
    try {
      replace = MiscUtils.getaVehicleFromArgs(Arrays.copyOfRange(args, 1, args.length), storage);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }
    Vehicle old = storage.getElement(id);
    try {
      if (old.compareTo(replace) < 0) {
        storage.updateElement(old.getId(), replace);
        return "Replaced 1 item";
      } else {
        return "Replaced 0 items";
      }
    } catch (SQLException e) {
      return "! Database error: " + e.getMessage();
    }
  }
}
