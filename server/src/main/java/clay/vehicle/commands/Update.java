package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import jakarta.validation.ValidationException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Command implementation for updating an existing vehicle. This command updates a vehicle with the
 * specified ID by replacing it with a new vehicle that has the same ID but potentially different
 * attributes.
 */
public class Update implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new Update command with the specified storage.
   *
   * @param storage the storage where vehicles will be updated
   */
  public Update(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the update command. Takes a vehicle ID as argument and prompts for new vehicle
   * details. Updates the existing vehicle with the new details while preserving the ID.
   *
   * @param args command arguments, where args[0] is the ID of the vehicle to update
   * @return a message indicating the result of the update operation
   */
  @Override
  public String execute(String[] args) {
    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! id is not valid";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    Vehicle old = storage.getElement(id);

    if (old.getUserId() != Integer.parseInt(args[args.length - 1])) {
      return "! Permission error";
    }

    Vehicle update;
    try {
      update = MiscUtils.getaVehicleFromArgs(Arrays.copyOfRange(args, 1, args.length), storage);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }
    update.setId(id);

    try {
      storage.insert(update);
    } catch (SQLException e) {
      return "! Database error: " + e.getMessage();
    }

    return "Updated id " + id + ": " + update;
  }
}
