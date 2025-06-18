package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;

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
   * Executes the print_ascending command with arguments. Returns a formatted string containing all
   * vehicles in ascending order.
   *
   * @param args command arguments (not used)
   * @return a string containing all vehicles in ascending order
   */
  @Override
  public String execute(String[] args) {
    StringBuilder builder = new StringBuilder();
    for (Vehicle v : storage.getValues().stream().sorted().toList()) {

      builder.append(String.format("%d | %s", v.getId(), v + "\n"));
    }
    builder.append("\n");
    builder.append("Total ").append(storage.getLen()).append(" elements");
    return builder.toString();
  }
}
