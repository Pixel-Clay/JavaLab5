package clay.vehicle;

import clay.vehicle.commands.Executable;
import clay.vehicle.commands.MiscUtils;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Processes and executes commands in the Vehicle Management System. This class maintains a list of
 * instructions and a mapping of command names to their executable implementations.
 */
public class CommandProcessor {

  private final List<String> instructions = new ArrayList<>();

  @Getter @Setter private Map<String, Executable> commands = new HashMap<>();

  /**
   * Adds a new instruction to the processing queue.
   *
   * @param instruction The command instruction to be processed
   */
  public synchronized void addInstruction(String instruction) {
    instructions.add(instruction);
  }

  /**
   * Attaches a command to the processor with a specific name.
   *
   * @param cmd The executable command to attach
   * @param name The name/keyword that will be used to invoke this command
   */
  public synchronized void attachCommand(Executable cmd, String name) {
    commands.put(name, cmd);
  }

  /** Clears the command queue */
  public synchronized void clearQueue() {
    instructions.clear();
  }

  public synchronized String runReturnable() throws InvalidInstructionException {
    String instruction;
    StringBuilder builder = new StringBuilder();
    for (int idx = 0; idx < instructions.toArray().length; idx++) {
      instruction = instructions.get(idx);
      String[] splitInstruction = MiscUtils.splitQuoted(instruction);
      Executable curCommand = commands.get(splitInstruction[0]);
      if (curCommand == null) {
        instructions.clear();
        throw new InvalidInstructionException(idx + 1 + ": " + instruction);
      } else
        builder.append(
            curCommand.execute(Arrays.copyOfRange(splitInstruction, 1, splitInstruction.length)));
      builder.append("\n");
    }
    instructions.clear();
    return builder.toString();
  }
}
