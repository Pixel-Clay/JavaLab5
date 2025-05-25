package SerializeTest;

import clay.vehicle.commands.Show;
import clay.vehicle.dataStorage.CsvReader;
import clay.vehicle.dataStorage.CsvWriter;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;

public class Main {
  public static void main(String[] args) {
    VehicleStorage storage = new VehicleStorage();
    storage.getNextId();

    Vehicle car1 =
        new Vehicle(
            1,
            "car1",
            new Coordinates(3.00, 4.00),
            ZonedDateTime.now(),
            0.7F,
            20F,
            VehicleType.CAR,
            FuelType.ALCOHOL);

    Vehicle car2 =
        new Vehicle(
            2,
            "hoverboard",
            new Coordinates(75.89, 2),
            ZonedDateTime.now(),
            20F,
            10F,
            VehicleType.HOVERBOARD,
            FuelType.MANPOWER);

    storage.insert(car1);
    storage.insert(car2);

    Path outputPath = Paths.get("data/output/vehicles.csv");

    // Write to CSV using the CsvWriter
    try {
      CsvWriter.writeVehicleStorageToCsv(storage, outputPath);
    } catch (IOException e) {
      e.printStackTrace();
    }

    VehicleStorage new_storage = null;
    // Test loading
    try {
      new_storage = CsvReader.readVehicleStorageFromCsv(outputPath);
      System.out.println("Successfully loaded " + new_storage.getStorage().size() + " vehicles");
    } catch (IOException e) {
      e.printStackTrace();
    }

    Show show = new Show(new_storage);
    System.out.println(show.execute());
  }
}
