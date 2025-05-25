package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.dataStorage.VehicleStorage;

/**
 * Command implementation for displaying all vehicles in the storage. This command shows all
 * vehicles in the storage with their IDs and details.
 */
public class Show implements Executable {
  /** The storage instance containing the vehicles to display */
  VehicleStorage storage;

  /**
   * Constructs a new Show command with the specified storage.
   *
   * @param storage the storage containing the vehicles to display
   */
  public Show(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the show command with arguments. Currently delegates to the no-argument version.
   *
   * @param args command arguments (not used)
   * @return a string containing all vehicles in the storage
   */
  @Override
  public String execute(String[] args) {
    return execute();
  }

  /**
   * Executes the show command without arguments. Returns a formatted string containing all vehicles
   * in the storage.
   *
   * @return a string containing all vehicles and their details
   */
  public String execute() {
    StringBuilder builder = new StringBuilder();
    for (Integer id : storage.getKeys()) {
      builder.append(String.format("%d | %s", id, storage.getElement(id).toString()) + "\n");
    }
    builder.append("\n");
    builder.append("Total " + storage.getLen() + " elements");
    return builder.toString();
  }

  /**
   * Attaches a shell instance to this command. This command doesn't require shell access, so this
   * method is empty.
   *
   * @param newShell the shell instance to attach (not used)
   */
  @Override
  public void attachShell(Shell newShell) {}
}
