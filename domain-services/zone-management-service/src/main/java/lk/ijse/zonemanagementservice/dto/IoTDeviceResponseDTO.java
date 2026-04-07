package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Capture response from the external IoT device registration endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IoTDeviceResponseDTO {
    private String deviceId;
}
