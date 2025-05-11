package clay.vehicle.dataStorage;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class VehicleStorageSerializer extends JsonSerializer<VehicleStorage> {
  @Override
  public void serialize(VehicleStorage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    List<VehicleStorageCsvRow> rows = value.getStorage().entrySet().stream()
            .map(entry -> new VehicleStorageCsvRow(
                    entry.getKey(),
                    entry.getValue(),
                    value.getInitDate()
            ))
            .toList();

    serializers.defaultSerializeValue(rows, gen);
  }
}