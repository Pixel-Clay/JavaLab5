package clay.vehicle.commands;

/** This exception gets thrown when recursion is detected during execution of a script */
public class RecursionException extends RuntimeException {
  public RecursionException(String message) {
    super(message);
  }
}
