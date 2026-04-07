package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration details for the external IoT API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IoTRegisterRequestDTO {
    private String username;
    private String password;
}
