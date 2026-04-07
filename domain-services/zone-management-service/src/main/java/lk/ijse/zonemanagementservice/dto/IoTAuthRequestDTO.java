package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Credentials sent to the external IoT provider's auth endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IoTAuthRequestDTO {
    private String username;
    private String password;
}
