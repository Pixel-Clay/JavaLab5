package clay.vehicle.dataStorage;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/**
 * Serializer for VehicleStorage objects. This serializer converts VehicleStorage objects into a
 * list of VehicleStorageCsvRow objects for CSV serialization, maintaining the relationship between
 * vehicles and their initialization dates.
 */
public class VehicleStorageSerializer extends JsonSerializer<VehicleStorage> {
  /**
   * Serializes a VehicleStorage object. Converts the storage entries into a list of
   * VehicleStorageCsvRow objects, each containing a vehicle and its initialization date.
   *
   * @param value the VehicleStorage object to serialize
   * @param gen the JsonGenerator to use for writing the output
   * @param serializers the SerializerProvider to use for finding serializers
   * @throws IOException if an I/O error occurs during serialization
   */
  @Override
  public void serialize(VehicleStorage value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    List<VehicleStorageCsvRow> rows =
        value.getStorage().values().stream()
            .map(vehicle -> new VehicleStorageCsvRow(vehicle, value.getInitDate()))
            .toList();

    serializers.defaultSerializeValue(rows, gen);
  }
}
