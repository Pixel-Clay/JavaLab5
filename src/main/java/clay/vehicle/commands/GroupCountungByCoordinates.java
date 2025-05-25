package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Command implementation for grouping vehicles by their coordinates and counting them. This command
 * groups all vehicles in the storage by their coordinates and returns a count of vehicles for each
 * unique coordinate value.
 */
public class GroupCountungByCoordinates implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new GroupCountungByCoordinates command with the specified storage.
   *
   * @param storage the storage containing the vehicles to be grouped
   */
  public GroupCountungByCoordinates(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the group_counting_by_coordinates command. Groups all vehicles by their coordinates
   * and counts how many vehicles share each unique coordinate value.
   *
   * @param args command arguments (not used)
   * @return a string containing coordinate values and their corresponding vehicle counts
   */
  @Override
  public String execute(String[] args) {
    ArrayList<Vehicle> vehicles = storage.getValues();
    HashMap<String, ArrayList<Vehicle>> groups = new HashMap<>();
    for (Vehicle v : vehicles) {
      if (!groups.containsKey(v.getCoordinates().toString())) {
        groups.put(v.getCoordinates().toString(), new ArrayList<>());
        groups.get(v.getCoordinates().toString()).add(v);
      } else {
        groups.get(v.getCoordinates().toString()).add(v);
      }
    }

    StringBuilder builder = new StringBuilder();

    for (String c : groups.keySet()) {
      builder.append(c);
      builder.append(": ");
      builder.append(groups.get(c).size());
      builder.append("\n");
    }
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
