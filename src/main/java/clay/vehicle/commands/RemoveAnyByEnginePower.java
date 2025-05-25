package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Vehicle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command implementation for removing a vehicle with a specific engine power. This command removes
 * one vehicle from the storage that has the specified engine power. If multiple vehicles have the
 * same engine power, only one is removed.
 */
public class RemoveAnyByEnginePower implements Executable {
  /** The storage instance where vehicles are stored */
  VehicleStorage storage;

  /** The shell instance for input/output operations */
  Shell shell;

  /**
   * Constructs a new RemoveAnyByEnginePower command with the specified storage.
   *
   * @param storage the storage from which vehicles will be removed
   */
  public RemoveAnyByEnginePower(VehicleStorage storage) {
    this.storage = storage;
  }

  /**
   * Executes the remove_any_by_engine_power command. Removes one vehicle that has the specified
   * engine power.
   *
   * @param args command arguments, where args[0] is the engine power value
   * @return a message indicating whether a vehicle was removed
   */
  @Override
  public String execute(String[] args) {
    Float enginePower;
    try {
      enginePower = Float.parseFloat(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid argument";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    Set<Integer> lowerIds =
        this.storage.getCollection().values().stream()
            .filter(vehicle -> vehicle.getEnginePower().equals(enginePower))
            .map(Vehicle::getId)
            .collect(Collectors.toSet());

    if (lowerIds.toArray().length == 0) return "Removed 0 items";
    else {
      storage.removeKey(lowerIds.iterator().next());
      return "Removed 1 item";
    }
  }

  /**
   * Attaches a shell instance to this command.
   *
   * @param newShell the shell instance to attach
   */
  @Override
  public void attachShell(Shell newShell) {
    this.shell = newShell;
  }
}
