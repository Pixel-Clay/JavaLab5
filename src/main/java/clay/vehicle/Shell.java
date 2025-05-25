package clay.vehicle;

import clay.vehicle.commands.Executable;
import java.util.Scanner;
import lombok.Getter;

/**
 * A command-line shell implementation for the Vehicle Management System. This class provides an
 * interactive command-line interface for users to interact with the vehicle management commands.
 */
public class Shell {
  Scanner scanner = new Scanner(System.in);

  @Getter CommandProcessor processor = new CommandProcessor();

  /**
   * Attaches a command to the shell and registers it with the command processor.
   *
   * @param command The executable command to attach
   * @param name The name/keyword that will be used to invoke this command
   */
  public void attachCommand(Executable command, String name) {
    command.attachShell(this);
    processor.attachCommand(command, name);
  }

  /**
   * Gets user input from the console with a prompt. Handles EOF (End of File) conditions
   * gracefully.
   *
   * @param invitation The prompt text to display to the user
   * @return The user's input line, or null if empty line was entered
   */
  public String getInput(String invitation) {
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

  /**
   * Runs the interactive shell loop. Continuously prompts for user input, processes commands, and
   * handles any invalid instructions.
   */
  public void run() {
    String line;
    while (true) {
      line = getInput("> ");
      if (line == null) continue;
      processor.addInstruction(line);
      try {
        processor.run();
      } catch (InvalidInstructionException e) {
        System.out.println("! Unknown command");
      }
    }
  }
}
