package clay.vehicle.dataStorage;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for writing vehicle data to CSV files. This class provides functionality to
 * serialize VehicleStorage objects to CSV format using Jackson's CSV mapping capabilities.
 */
public class CsvWriter {
  /**
   * Writes a VehicleStorage object to a CSV file. The method performs the following steps: 1.
   * Configures the CSV mapper with appropriate modules and settings 2. Creates any necessary parent
   * directories for the output file 3. Serializes the storage data to CSV format 4. Writes the data
   * to the specified file
   *
   * @param storage the VehicleStorage object to write
   * @param outputPath the path where the CSV file should be written
   * @return a message indicating where the CSV file was written
   * @throws IOException if there are issues creating directories or writing the file
   */
  public static String writeVehicleStorageToCsv(VehicleStorage storage, Path outputPath)
      throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    csvMapper.registerModule(new JavaTimeModule()); // Handles ZonedDateTime
    csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Define CSV schema based on VehicleStorageCsvRow
    CsvSchema schema = MapperSchema.schema;

    // Create parent directories if they don't exist
    Files.createDirectories(outputPath.getParent());

    // Convert Path to File for compatibility with Jackson's CsvWriter
    File outputFile = outputPath.toFile();

    // Write to CSV using the CsvWriter
    csvMapper.writer(schema).writeValue(outputFile, storage);

    return "CSV written to: " + outputPath.toAbsolutePath();
  }
}
