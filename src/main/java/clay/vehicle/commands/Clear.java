package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;

/**
 * Command implementation for clearing the vehicle storage. This command removes all vehicles from
 * the storage.
 */
public class Clear extends ExecutableRequiresShell {

  /** The storage instance to be cleared */
  VehicleStorage storage;

  /**
   * Constructs a new Clear command with the specified storage.
   *
   * @param storage the storage to be cleared
   */
  public Clear(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the clear command. Removes all vehicles from the storage.
   *
   * @param args command arguments (not used)
   * @return a message indicating that the collection was cleared
   */
  @Override
  public String execute(String[] args) {
    storage.clearCollection();
    return "Cleared collection";
  }
}
