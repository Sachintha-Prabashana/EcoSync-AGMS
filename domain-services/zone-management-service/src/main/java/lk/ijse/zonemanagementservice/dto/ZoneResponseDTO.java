package lk.ijse.zonemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned to callers when querying or creating a Zone.
 * Includes all threshold data plus the external IoT deviceId.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponseDTO {

    private Long id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private Double minHumidity;
    private Double maxHumidity;

    /** Linked device ID from the external IoT provider */
    private String deviceId;
}
