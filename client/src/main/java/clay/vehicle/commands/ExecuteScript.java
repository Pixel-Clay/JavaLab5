package clay.vehicle.commands;

import clay.vehicle.ClientShell;
import clay.vehicle.CommandProcessor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command implementation for executing a script file containing multiple commands. This command
 * reads a script file line by line and executes each command in sequence. The script file should
 * contain one command per line.
 */
public class ExecuteScript implements Executable {
  /** The command processor used to execute script commands */
  CommandProcessor processor;

  ClientShell shell;

  static List<Path> callStack = new ArrayList<>();

  /**
   * Constructs a new ExecuteScript command with the specified command processor. Creates a new
   * command processor that shares the same command mappings as the provided one.
   *
   * @param processor the command processor whose commands will be used
   */
  public ExecuteScript(CommandProcessor processor, ClientShell shell) {
    this.processor = processor;
    this.shell = shell;
  }

  /**
   * Executes the execute_script command. Reads the specified script file and executes each command
   * in sequence. The script file should contain one command per line. Recursion is not allowed.
   *
   * @param args command arguments, where args[0] is the path to the script file
   * @return a message indicating the result of script execution
   */
  @Override
  public String execute(String[] args) throws RecursionException {
    shell.setRecordedInputFlag();

    Path path;
    String script;

    if (args.length < 1) return "! Not enough arguments";

    path = Path.of(args[0]);

    if (callStack.contains(path)) {
      throw new RecursionException(callStack.toString());
    } else callStack.add(path);

    try (FileInputStream fis = new FileInputStream(path.toFile());
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
      StringBuilder builder = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        builder.append(line).append("\n");
      }

      // Convert all bytes to a single String
      script = builder.toString();

      Pattern pattern = Pattern.compile(path.toFile().getAbsolutePath().replace("\\", "/") + "\\z");

      for (String l : script.split("\n")) {
        if (l.startsWith(";")) continue; // comments
        if (l.startsWith(":")) { // user input
          if (l.length() <= 2) shell.addToInputBuffer(null);
          else shell.addToInputBuffer(l.substring(2));
          continue;
        }

        l = l.strip();

        if (l.isEmpty()) continue;

        if (Objects.equals(l.split(" ")[0], "execute_script")) {
          Matcher matcher = pattern.matcher(l.replace("\\", "/"));
          if (matcher.find()) throw new RecursionException(callStack.toString());
        }

        processor.addInstruction(l);
      }

    } catch (IOException e) {
      return "! IOException: " + e.getMessage();
    }

    try {
      callStack.add(path);

    } catch (RecursionException e) {
      callStack.clear();
      throw new RecursionException(e.getMessage());
    }
    callStack.remove(path);
    return "! Running script...";
  }
}
