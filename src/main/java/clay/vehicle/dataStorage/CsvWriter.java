package clay.vehicle.dataStorage;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvWriter {
  public static String writeVehicleStorageToCsv(VehicleStorage storage, Path outputPath)
      throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    csvMapper.registerModule(new JavaTimeModule()); // Handles ZonedDateTime
    csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Define CSV schema based on VehicleStorageCsvRow
    CsvSchema schema =
        CsvSchema.builder()
            .addColumn("license_plate")
            .addColumn("id")
            .addColumn("name")
            .addColumn("x")
            .addColumn("y")
            .addColumn("creationDate")
            .addColumn("enginePower")
            .addColumn("distanceTravelled")
            .addColumn("type")
            .addColumn("fuelType")
            .addColumn("init_date")
            .build()
            .withHeader();

    // Create parent directories if they don't exist
    Files.createDirectories(outputPath.getParent());

    // Convert Path to File for compatibility with Jackson's CsvWriter
    File outputFile = outputPath.toFile();

    // Write to CSV using the CsvWriter
    csvMapper.writer(schema).writeValue(outputFile, storage);

    return "CSV written to: " + outputPath.toAbsolutePath();
  }
}
