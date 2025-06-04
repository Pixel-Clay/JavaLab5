package clay.vehicle.commands;

/**
 * Command implementation for exiting the application. This command terminates the program when
 * executed.
 */
public class Exit extends ExecutableRequiresShell {

  /**
   * Executes the exit command. Terminates the program with exit code 0.
   *
   * @param args command arguments (not used)
   * @return an empty string (never reached as the program exits)
   */
  @Override
  public String execute(String[] args) {
    shell.setExitFlag();
    return "Exiting...";
  }
}
