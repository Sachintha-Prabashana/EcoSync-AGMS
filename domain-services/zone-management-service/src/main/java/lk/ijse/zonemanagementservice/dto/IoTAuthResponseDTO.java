package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Capture response from the external IoT auth endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IoTAuthResponseDTO {
    private String token;
}
