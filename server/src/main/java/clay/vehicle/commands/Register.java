package clay.vehicle.commands;

import clay.vehicle.dataStorage.DbStoreManager;
import java.sql.SQLException;

/**
 * Command implementation for removing vehicles that are less than a given example. This command
 * prompts for an example vehicle and removes all vehicles from the storage that are less than the
 * example according to their natural ordering.
 */
public class Register implements Executable {
  /** The storage instance where vehicles are stored */
  DbStoreManager pg;

  /** Constructs a new RemoveLower command with the specified storage. */
  public Register(DbStoreManager pg) {
    this.pg = pg;
  }

  /**
   * Executes the remove_lower command. Prompts for an example vehicle and removes all vehicles that
   * are less than it.
   *
   * @param args command arguments (not used)
   * @return a message indicating how many vehicles were removed
   */
  @Override
  public String execute(String[] args) {
    if (args.length < 2) {
      return "! Not enough arguments";
    }
    try {
      pg.createUser(args[0], args[1]);
    } catch (SQLException e) {
      return "! Database error: " + e.getMessage();
    }

    return "Successfully registered user " + args[0];
  }
}
