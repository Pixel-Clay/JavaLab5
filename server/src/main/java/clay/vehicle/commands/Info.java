package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;

/**
 * Command implementation for displaying information about the vehicle storage. This command shows
 * the initialization date, type, and number of vehicles in the storage.
 */
public class Info extends ExecutableRequiresShell {
  /** The storage instance to get information about */
  VehicleStorage storage;

  /**
   * Constructs a new Info command with the specified storage.
   *
   * @param storage the storage to get information about
   */
  public Info(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the info command. Returns a formatted string containing information about the storage.
   *
   * @param args command arguments (not used)
   * @return a string containing storage information (initialization date, type, and size)
   */
  @Override
  public String execute(String[] args) {
    return String.format(
        """
                        Init: %s
                        Type: %s
                        Len: %d""",
        storage.getInitDate(), storage.getType(), storage.getLen());
  }
}
