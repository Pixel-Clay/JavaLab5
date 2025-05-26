package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import jakarta.validation.ValidationException;

/**
 * Command implementation for inserting a new vehicle into the storage. This command prompts the
 * user for vehicle details and adds the new vehicle to the storage if all validations pass.
 */
public class Insert extends ExecutableRequiresShell {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new Insert command with the specified storage.
   *
   * @param storage the storage where vehicles will be inserted
   */
  public Insert(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the insert command. Prompts the user for vehicle details, validates them, and inserts
   * the new vehicle into the storage if validation passes.
   *
   * @param args command arguments (not used)
   * @return a message indicating the result of the operation
   */
  @Override
  public String execute(String[] args) {
    Vehicle v;
    try {
      v = MiscUtils.getaVehicleFromInput(shell, storage);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }

    storage.insert(v);
    return "Inserted new " + v;
  }
}
