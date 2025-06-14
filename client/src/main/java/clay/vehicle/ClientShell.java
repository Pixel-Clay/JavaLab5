package clay.vehicle;

import clay.vehicle.commands.Executable;
import clay.vehicle.commands.RecursionException;
import clay.vehicle.networking.ClientNetworkingManager;
import clay.vehicle.networking.NetworkMessageDeserializer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import lombok.Getter;

/**
 * A command-line shell implementation for the Vehicle Management System. This class provides an
 * interactive command-line interface for users to interact with the vehicle management commands.
 */
public class ClientShell implements Shell {
  Scanner scanner = new Scanner(System.in);

  @Getter CommandProcessor processor = new CommandProcessor();

  private ClientNetworkingManager nm;

  private Boolean exitFlag = false;
  private Boolean recordedInputFlag = false;

  private Queue<String> recordedInputBuffer = new LinkedList<>();

  public void attachNetworking(ClientNetworkingManager nm) {
    this.nm = nm;
  }

  /** Sets a flag that tells the shell to exit */
  @Override
  public void setExitFlag() {
    exitFlag = true;
  }

  /**
   * Sets a flag that tells the shell to switch input to recorded mode. Next input queries will have
   * their answers automatically populated from recordedInputBuffer
   */
  public void setRecordedInputFlag() {
    recordedInputFlag = true;
  }

  /**
   * Adds recorded user input to recordedInputBuffer
   *
   * @param input recorded user input
   */
  public void addToInputBuffer(String input) {
    recordedInputBuffer.add(input);
  }

  /**
   * Attaches a command to the shell and registers it with the command processor.
   *
   * @param command The executable command to attach
   * @param name The name/keyword that will be used to invoke this command
   */
  @Override
  public void attachCommand(Executable command, String name) {
    processor.attachCommand(command, name);
  }

  /**
   * Gets user input from the console with a prompt. Handles EOF (End of File) conditions
   * gracefully.
   *
   * @param invitation The prompt text to display to the user
   * @return The user's input line, or null if empty line was entered
   */
  @Override
  public String getInput(String invitation) {
    if (recordedInputFlag && !recordedInputBuffer.isEmpty()) {
      try {
        return recordedInputBuffer.remove().strip();
      } catch (NullPointerException e) {
        return null;
      }
    } else {
      System.out.print(invitation);

      // handle EOF
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().strip();
        if (line.isEmpty()) return null;
        else return line;
      }

      System.out.println("EOF reached. Exiting...");
      scanner.close();
      System.exit(0);
      return "";
    }
  }

  /**
   * Runs the interactive shell loop. Continuously prompts for user input, processes commands, and
   * handles any invalid instructions.
   */
  @Override
  public void run() {
    recordedInputFlag = false;
    recordedInputBuffer.clear();
    String ret;

    String line;
    while (!exitFlag) {
      line = getInput("> ");
      if (line == null) continue;
      processor.addInstruction(line);
      try {
        ret = processor.runReturnable();
        for (String cmd : ret.split("\n")) {
          if (cmd.charAt(0) == '!') System.out.println(cmd);
          else {

            nm.transmit(cmd);

            System.out.println(NetworkMessageDeserializer.deserialize(nm.receive()).getMessage());
          }
        }
      } catch (InvalidInstructionException e) {
        System.out.println("! Unknown command");
      } catch (RecursionException e) {
        processor.clearQueue();
        System.out.println("! Recursion not allowed. Call stack: " + e.getMessage());
      } catch (IOException e) {
        System.out.println("! IO exception: " + e.getMessage());
      }
    }
  }
}
