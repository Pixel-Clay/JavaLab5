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
    VehicleStorage newStorage = null;
    Path outputPath = null;

    try {
      outputPath = EnvPathRetriever.getPath("CLAY_VEHICLE_DATA_PATH");
    } catch (NoEnvVarFoundException e) {
      System.out.println("Env var not found, set one at $CLAY_VEHICLE_DATA_PATH");
      System.exit(-1);
    }
    try {
      newStorage = CsvReader.readVehicleStorageFromCsv(outputPath);
    } catch (FileNotFoundException e) {
      System.out.println("Database not found. Exiting...");
      System.exit(-2);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Critical IO exception: " + e.getMessage() + ". Exiting...");
      System.exit(-3);
    }
    System.out.println(
        "Successfully loaded " + newStorage.getStorage().size() + " vehicles after validation");

    Shell shell = new Shell();

    shell.attachCommand(new Exit(), "exit");
    shell.attachCommand(new Info(newStorage), "info");
    shell.attachCommand(new Show(newStorage), "show");
    shell.attachCommand(new Help(), "help");
    shell.attachCommand(new RemoveKey(newStorage), "remove_key");
    shell.attachCommand(new Insert(newStorage), "insert");
    shell.attachCommand(new Update(newStorage), "update");
    shell.attachCommand(new Clear(newStorage), "clear");
    shell.attachCommand(new Save(newStorage, outputPath), "save");
    shell.attachCommand(new ExecuteScript(shell.getProcessor()), "execute_script");
    shell.attachCommand(new RemoveLower(newStorage), "remove_lower");
    shell.attachCommand(new ReplaceIfHigher(newStorage), "replace_if_greater");
    shell.attachCommand(new RemoveLowerKey(newStorage), "remove_lower_key");
    shell.attachCommand(new RemoveAnyByEnginePower(newStorage), "remove_any_by_engine_power");
    shell.attachCommand(new PrintAscending(newStorage), "print_ascending");
    shell.attachCommand(
        new GroupCountungByCoordinates(newStorage), "group_counting_by_coordinates");

    shell.run();
  }
}
