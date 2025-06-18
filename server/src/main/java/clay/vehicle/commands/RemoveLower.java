package clay.vehicle.commands;

import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import jakarta.validation.ValidationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command implementation for removing vehicles that are less than a given example. This command
 * prompts for an example vehicle and removes all vehicles from the storage that are less than the
 * example according to their natural ordering.
 */
public class RemoveLower implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /**
   * Constructs a new RemoveLower command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveLower(VehicleStorage storage) {
    this.storage = storage;
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
    Vehicle example;
    try {
      example = MiscUtils.getaVehicleFromArgs(args, storage);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }
    Integer counter = 0;

    Set<Integer> lowerIds =
        this.storage.getCollection().values().stream()
            .filter(route -> route.compareTo(example) < 0)
            .map(Vehicle::getId)
            .collect(Collectors.toSet());

    for (Integer i : lowerIds) {
      storage.removeKey(i);
      counter++;
    }

    return "Removed " + counter + " items";
  }
}
