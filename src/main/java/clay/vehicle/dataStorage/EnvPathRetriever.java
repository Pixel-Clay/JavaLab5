package clay.vehicle.dataStorage;

public class EnvPathRetriever {
  String varName;

  public EnvPathRetriever(String varName) {
    this.varName = varName;
  }

  public String getPath() throws NoEnvVarFoundException {
    String path = System.getenv("CLAY_VEHICLE_DATA_PATH");
    if (path == null) {
      throw new NoEnvVarFoundException("Env var " + this.varName + " does not exist");
    }
    return path;
  }
}
