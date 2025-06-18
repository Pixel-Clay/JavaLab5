package clay.vehicle;

import clay.vehicle.commands.*;
import clay.vehicle.networking.ClientNetworkingManager;
import java.io.IOException;
import java.net.UnknownHostException;

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
    ClientNetworkingManager networkingManager = null;
    String hostname = null;
    Integer port = null;

    if (args.length < 2) {
      System.out.println("Usage: client server_address port");
      System.exit(0);
    }

    hostname = args[0];

    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println("! Invalid port");
      System.exit(-1);
    }

    if (port < 0 && port > 65535) {
      System.out.println("! Invalid port");
      System.exit(-1);
    }
    CommandProcessor processor = new CommandProcessor();
    ClientShell shell = new ClientShell(processor);

    shell.attachCommand(new Exit(shell), "exit");
    shell.attachCommand(new Info(), "info");
    shell.attachCommand(new Show(), "show");
    shell.attachCommand(new Help(), "help");
    shell.attachCommand(new RemoveKey(), "remove_key");
    shell.attachCommand(new Insert(shell), "insert");
    shell.attachCommand(new Update(shell), "update");
    shell.attachCommand(new Clear(), "clear");
    shell.attachCommand(new ExecuteScript(processor, shell), "execute_script");
    shell.attachCommand(new RemoveLower(shell), "remove_lower");
    shell.attachCommand(new ReplaceIfHigher(shell), "replace_if_greater");
    shell.attachCommand(new RemoveLowerKey(), "remove_lower_key");
    shell.attachCommand(new RemoveAnyByEnginePower(), "remove_any_by_engine_power");
    shell.attachCommand(new PrintAscending(), "print_ascending");
    shell.attachCommand(new GroupCountungByCoordinates(), "group_counting_by_coordinates");
    try {
      networkingManager = new ClientNetworkingManager(hostname, port);
      networkingManager.init();
      networkingManager.setTimeout(5000);
    } catch (UnknownHostException e) {
      System.out.println("! Invalid hostname");
      System.exit(-2);
    } catch (IOException e) {
      System.out.println("! IO exception");
      System.exit(-3);
    }

    shell.attachNetworking(networkingManager);
    shell.run();
  }
}
