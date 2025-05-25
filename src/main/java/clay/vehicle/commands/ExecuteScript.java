package clay.vehicle.commands;

import clay.vehicle.CommandProcessor;
import clay.vehicle.InvalidInstructionException;
import clay.vehicle.Shell;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Command implementation for executing a script file containing multiple commands. This command
 * reads a script file line by line and executes each command in sequence. The script file should
 * contain one command per line.
 */
public class ExecuteScript implements Executable {
  /** The command processor used to execute script commands */
  CommandProcessor processor;

  /**
   * Constructs a new ExecuteScript command with the specified command processor. Creates a new
   * command processor that shares the same command mappings as the provided one.
   *
   * @param processor the command processor whose commands will be used
   */
  public ExecuteScript(CommandProcessor processor) {
    this.processor = new CommandProcessor();
    this.processor.setCommands(processor.getCommands());
  }

  /**
   * Executes the execute_script command. Reads the specified script file and executes each command
   * in sequence. The script file should contain one command per line.
   *
   * @param args command arguments, where args[0] is the path to the script file
   * @return a message indicating the result of script execution
   */
  @Override
  public String execute(String[] args) {
    Path path;
    String script;
    try {
      path = Path.of(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    try (FileInputStream fis = new FileInputStream(path.toFile());
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;

      // Read bytes and write to ByteArrayOutputStream
      while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
      }

      // Convert all bytes to a single String
      script = bos.toString();

      for (String line : script.split("\n")) {
        line = line.strip();
        if (line.isEmpty()) continue;
        processor.addInstruction(line);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      processor.run();
    } catch (InvalidInstructionException e) {
      return "Invalid command at line " + e.getMessage();
    }

    return "Script executed successfully";
  }

  /**
   * Attaches a shell instance to this command. This command doesn't require shell access, so this
   * method is empty.
   *
   * @param newShell the shell instance to attach (not used)
   */
  @Override
  public void attachShell(Shell newShell) {}
}
