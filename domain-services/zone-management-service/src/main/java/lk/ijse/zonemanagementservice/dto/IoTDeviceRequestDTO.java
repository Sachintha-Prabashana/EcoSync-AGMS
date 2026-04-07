package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Register a device with the external IoT API.
 * Matching documented payload: { "name": "...", "zoneId": "..." }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IoTDeviceRequestDTO {
    private String name;
    private String zoneId;
}
