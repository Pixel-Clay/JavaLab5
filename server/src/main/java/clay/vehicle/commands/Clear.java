package clay.vehicle.commands;

import clay.vehicle.dataStorage.DbStoreManager;
import clay.vehicle.dataStorage.Storage;
import java.sql.SQLException;

/**
 * Command implementation for clearing the vehicle storage. This command removes all vehicles from
 * the storage.
 */
public class Clear implements Executable {

  /** The storage instance to be cleared */
  Storage storage;

  DbStoreManager manager;

  /**
   * Constructs a new Clear command with the specified storage.
   *
   * @param storage the storage to be cleared
   */
  public Clear(Storage storage, DbStoreManager manager) {
    this.storage = storage;
    this.manager = manager;
  }

  /**
   * Executes the clear command. Removes all vehicles from the storage.
   *
   * @param args command arguments (not used)
   * @return a message indicating that the collection was cleared
   */
  @Override
  public String execute(String[] args) {
    if (Integer.parseInt(args[args.length - 1]) != 1) {
      return "! Permission error";
    }

    if (args[0].equals("users")) {
      try {
        manager.resetUsers();
        return "Reset user accounts. Now create admin account using register.";
      } catch (SQLException e) {
        return "! Database error: " + e.getMessage();
      }
    } else {
      try {
        storage.clearCollection();
        return "Cleared collection";
      } catch (SQLException e) {
        return "! Database error: " + e.getMessage();
      }
    }
  }
}
