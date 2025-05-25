package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.util.Iterator;

/**
 * Command implementation for displaying vehicles in ascending order. This command shows all
 * vehicles in the storage sorted by their natural ordering.
 */
public class PrintAscending implements Executable {
  /** The storage instance containing the vehicles to display */
  VehicleStorage storage;

  /**
   * Constructs a new PrintAscending command with the specified storage.
   *
   * @param storage the storage containing the vehicles to display
   */
  public PrintAscending(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the print_ascending command with arguments. Currently delegates to the no-argument
   * version.
   *
   * @param args command arguments (not used)
   * @return a string containing all vehicles in ascending order
   */
  @Override
  public String execute(String[] args) {
    return execute();
  }

  /**
   * Executes the print_ascending command without arguments. Returns a formatted string containing
   * all vehicles in ascending order.
   *
   * @return a string containing all vehicles sorted in ascending order
   */
  public String execute() {
    StringBuilder builder = new StringBuilder();
    for (Iterator<Vehicle> it = storage.getValues().stream().sorted().iterator(); it.hasNext(); ) {
      Vehicle v = it.next();
      builder.append(String.format("%d | %s", v.getId(), v + "\n"));
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
