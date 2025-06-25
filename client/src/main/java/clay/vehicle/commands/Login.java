package clay.vehicle.commands;

import clay.vehicle.ClientShell;

/** Command implementation for user sign in */
public class Login implements Executable {
  /** The current ClientShell instance */
  ClientShell shell;

  /** Constructs a new login command with the specified shell. */
  public Login(ClientShell shell) {
    this.shell = shell;
  }

  /**
   * Executes the login command. Sets authentication parameter within client.
   *
   * @param args command arguments (not used)
   * @return a message indicating which user
   */
  @Override
  public String execute(String[] args) {
    String login, password;
    login = MiscUtils.getaStringNotNull(shell, "login: ");
    password = MiscUtils.getaStringNotNull(shell, "password: ");

    if (login.contains(" ") || password.contains(" "))
      return "! login and password should not contain spaces";
    else {
      shell.setLogin(login, password);
      return "! Logged in as " + login;
    }
  }
}
