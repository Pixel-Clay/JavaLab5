package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command implementation for removing vehicles with IDs less than a specified value. This command
 * removes all vehicles from the storage that have an ID less than the provided ID value.
 */
public class RemoveLowerKey implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new RemoveLowerKey command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveLowerKey(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the remove_lower_key command. Removes all vehicles with IDs less than the specified
   * ID.
   *
   * @param args command arguments, where args[0] is the ID threshold
   * @return a message indicating how many vehicles were removed
   */
  @Override
  public String execute(String[] args) {
    Integer id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid id";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    Integer counter = 0;

    Set<Integer> lowerIds =
        this.storage.getCollection().values().stream()
            .filter(vehicle -> vehicle.getId() < id)
            .map(Vehicle::getId)
            .collect(Collectors.toSet());

    for (Integer i : lowerIds) {
      storage.removeKey(i);
      counter++;
    }

    return "Removed " + counter + " items";
  }
}
