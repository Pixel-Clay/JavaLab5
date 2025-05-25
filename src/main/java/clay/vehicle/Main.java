package clay.vehicle;

import clay.vehicle.commands.*;
import clay.vehicle.dataStorage.CsvReader;
import clay.vehicle.dataStorage.EnvPathRetriever;
import clay.vehicle.dataStorage.NoEnvVarFoundException;
import clay.vehicle.dataStorage.VehicleStorage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * The main entry point for the Vehicle Management System. This class initializes the application,
 * loads vehicle data from a CSV file, and sets up the command shell with various vehicle management
 * commands.
 *
 * <p>The system requires an environment variable CLAY_VEHICLE_DATA_PATH to be set, pointing to the
 * CSV file containing vehicle data.
 */
public class Main {
  /**
   * The main method that initializes and runs the Vehicle Management System.
   *
   * <p>The method performs the following steps: 1. Retrieves the data file path from environment
   * variables 2. Loads vehicle data from the CSV file 3. Initializes the command shell 4. Registers
   * all available commands 5. Starts the interactive shell
   *
   * @param args Command line arguments (not used)
   */
  public static void main(String[] args) {
    VehicleStorage new_storage = null;
    Path outputPath = null;

    try {
      outputPath = EnvPathRetriever.getPath("CLAY_VEHICLE_DATA_PATH");
    } catch (NoEnvVarFoundException e) {
      System.out.println("Env var not found, set one at $CLAY_VEHICLE_DATA_PATH");
      System.exit(0);
    }
    try {
      new_storage = CsvReader.readVehicleStorageFromCsv(outputPath);
    } catch (FileNotFoundException e) {
      System.out.println("Database not found. Exiting...");
      System.exit(0);
    } catch (IOException e) {
      System.out.println("Critical IO exception. Exiting...");
      System.exit(0);
    }
    System.out.println(
        "Successfully loaded " + new_storage.getStorage().size() + " vehicles after validation");

    Shell shell = new Shell();

    shell.attachCommand(new Exit(), "exit");
    shell.attachCommand(new Info(new_storage), "info");
    shell.attachCommand(new Show(new_storage), "show");
    shell.attachCommand(new Help(), "help");
    shell.attachCommand(new RemoveKey(new_storage), "remove_key");
    shell.attachCommand(new Insert(new_storage), "insert");
    shell.attachCommand(new Update(new_storage), "update");
    shell.attachCommand(new Clear(new_storage), "clear");
    shell.attachCommand(new Save(new_storage, outputPath), "save");
    shell.attachCommand(new ExecuteScript(shell.getProcessor()), "execute_script");
    shell.attachCommand(new RemoveLower(new_storage), "remove_lower");
    shell.attachCommand(new ReplaceIfHigher(new_storage), "replace_if_greater");
    shell.attachCommand(new RemoveLowerKey(new_storage), "remove_lower_key");
    shell.attachCommand(new RemoveAnyByEnginePower(new_storage), "remove_any_by_engine_power");
    shell.attachCommand(new PrintAscending(new_storage), "print_ascending");
    shell.attachCommand(
        new GroupCountungByCoordinates(new_storage), "group_counting_by_coordinates");

    shell.run();
  }
}
