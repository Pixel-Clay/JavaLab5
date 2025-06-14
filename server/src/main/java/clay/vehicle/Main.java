package clay.vehicle;

import clay.vehicle.commands.*;
import clay.vehicle.dataStorage.CsvReader;
import clay.vehicle.dataStorage.EnvPathRetriever;
import clay.vehicle.dataStorage.NoEnvVarFoundException;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.networking.OnReadExecutionCallback;
import clay.vehicle.networking.ServerNetworkingManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * The main entry point for the Vehicle Management System. This class initializes the application,
 * loads vehicle data from a CSV file, and sets up the command executor with various vehicle
 * management commands.
 *
 * <p>The system requires an environment variable CLAY_VEHICLE_DATA_PATH to be set, pointing to the
 * CSV file containing vehicle data.
 */
public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class);

  /**
   * The main method that initializes and runs the Vehicle Management System.
   *
   * <p>The method performs the following steps: 1. Retrieves the data file path from environment
   * variables 2. Loads vehicle data from the CSV file 3. Initializes the command executor 4.
   * Registers all available commands 5. Starts the interactive executor
   *
   * @param args Command line arguments (not used)
   */
  public static void main(String[] args) {
    logger.info("Starting...");
    VehicleStorage newStorage = null;
    Path outputPath = null;

    if (args.length == 0) {
      logger.error("Server port not specified. Exiting...");
      System.exit(-4);
    }

    try {
      outputPath = EnvPathRetriever.getPath("CLAY_VEHICLE_DATA_PATH").toAbsolutePath();
    } catch (NoEnvVarFoundException e) {
      logger.error("Env var not found, set one at $CLAY_VEHICLE_DATA_PATH");
      System.exit(-1);
    }
    try {
      newStorage = CsvReader.readVehicleStorageFromCsv(outputPath);
    } catch (FileNotFoundException e) {
      logger.error("Database not found. Exiting...");
      System.exit(-2);
    } catch (IOException e) {
      logger.error("Critical IO exception: " + e.getMessage() + ". Exiting...");
      System.exit(-3);
    }
    logger.info(
        "Successfully loaded " + newStorage.getStorage().size() + " vehicles after validation");

    Save saver = new Save(newStorage, outputPath);

    OnReadExecutionCallback executor = new OnReadExecutionCallback();

    executor.attachCommand(new Info(newStorage), "info");
    executor.attachCommand(new Show(newStorage), "show");
    executor.attachCommand(new Help(), "help");
    executor.attachCommand(new RemoveKey(newStorage), "remove_key");
    executor.attachCommand(new Insert(newStorage), "insert");
    executor.attachCommand(new Update(newStorage), "update");
    executor.attachCommand(new Clear(newStorage), "clear");
    executor.attachCommand(new RemoveLower(newStorage), "remove_lower");
    executor.attachCommand(new ReplaceIfHigher(newStorage), "replace_if_greater");
    executor.attachCommand(new RemoveLowerKey(newStorage), "remove_lower_key");
    executor.attachCommand(new RemoveAnyByEnginePower(newStorage), "remove_any_by_engine_power");
    executor.attachCommand(new PrintAscending(newStorage), "print_ascending");
    executor.attachCommand(
        new GroupCountungByCoordinates(newStorage), "group_counting_by_coordinates");

    SignalHandler handler =
        signal -> {
          logger.info("Ctrl+C detected. Saving and shutting down...");
          saver.execute(new String[] {});
          System.exit(0); // Exit the program
        };

    // Register the handler for SIGINT
    Signal.handle(new Signal("INT"), handler);

    ServerNetworkingManager networkingManager = null;

    logger.info("Starting server...");

    try {
      networkingManager = new ServerNetworkingManager(Integer.parseInt(args[0]));
    } catch (BindException e) {
      logger.error("Specified port in use, try another one. Exiting...");
      System.exit(-5);
    } catch (IOException e) {
      logger.error("Critical IO exception: " + e);
      logger.error("Exiting...");
      System.exit(-6);
    }

    networkingManager.setReadCallback(executor);

    try {
      networkingManager.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
