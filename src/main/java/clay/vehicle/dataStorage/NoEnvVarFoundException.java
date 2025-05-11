package clay.vehicle.dataStorage;

public class NoEnvVarFoundException extends RuntimeException {
  public NoEnvVarFoundException(String message) {
    super(message);
  }
}
