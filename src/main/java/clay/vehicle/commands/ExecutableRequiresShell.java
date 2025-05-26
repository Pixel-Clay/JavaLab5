package clay.vehicle.commands;

import clay.vehicle.Shell;

public abstract class ExecutableRequiresShell implements Executable {
  /** The shell instance for input/output operations */
  Shell shell;

  /**
   * Attaches a shell instance to the command for input/output operations.
   *
   * @param newShell The shell instance to attach
   */
  @Override
  public void attachShell(Shell newShell) {
    this.shell = newShell;
  }
}
