package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Coordinates;
import clay.vehicle.vehicles.Vehicle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Command implementation for grouping vehicles by their coordinates and counting them. This command
 * groups all vehicles in the storage by their coordinates and returns a count of vehicles for each
 * unique coordinate value.
 */
public class GroupCountungByCoordinates extends ExecutableRequiresShell {
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
    Collection<Vehicle> vehicles = storage.getValues();
    HashMap<Coordinates, ArrayList<Vehicle>> groups = new HashMap<>();
    for (Vehicle v : vehicles) {
      if (!groups.containsKey(v.getCoordinates())) {
        groups.put(v.getCoordinates(), new ArrayList<>());
        groups.get(v.getCoordinates()).add(v);
      } else {
        groups.get(v.getCoordinates()).add(v);
      }
    }

    StringBuilder builder = new StringBuilder();

    for (Coordinates c : groups.keySet()) {
      builder.append(c.toString());
      builder.append(": ");
      builder.append(groups.get(c).size());
      builder.append("\n");
    }
    return builder.toString();
  }
}
