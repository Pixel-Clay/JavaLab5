package clay.vehicle.commands;

import clay.vehicle.dataStorage.CsvWriter;
import clay.vehicle.dataStorage.VehicleStorage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Command implementation for saving the vehicle storage to a CSV file. This command writes all
 * vehicles in the storage to a CSV file at the specified path.
 */
public class Save extends ExecutableRequiresShell {
  /** The storage instance containing the vehicles to save */
  VehicleStorage storage;

  /** The path where the CSV file will be saved */
  Path savePath;

  /**
   * Constructs a new Save command with the specified storage and file path.
   *
   * @param storage the storage containing the vehicles to save
   * @param filePath the path where the CSV file will be saved
   */
  public Save(VehicleStorage storage, Path filePath) {
    this.storage = storage;
    this.savePath = filePath;
  }

  /**
   * Executes the save command. Writes all vehicles in the storage to a CSV file.
   *
   * @param args command arguments (not used)
   * @return a message indicating the result of the save operation
   */
  @Override
  public String execute(String[] args) {
    // Write to CSV using the CsvWriter
    try {
      CsvWriter.writeVehicleStorageToCsv(storage, savePath);
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }

    return "Saved collection to " + savePath;
  }
}
