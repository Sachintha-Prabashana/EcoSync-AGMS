package lk.ijse.zonemanagementservice.service;

import lk.ijse.zonemanagementservice.dto.ZoneRequestDTO;
import lk.ijse.zonemanagementservice.dto.ZoneResponseDTO;

import java.util.List;

/**
 * Service interface for handling Greenhouse Zone management logic.
 */
public interface ZoneService {

    /**
     * Validates thresholds, performs IoT handshake (Register -> Login -> Register Device),
     * and saves the Zone record locally.
     * @param request the zone creation request
     * @return the saved zone details including deviceId
     */
    ZoneResponseDTO createZone(ZoneRequestDTO request);

    /**
     * Fetches a single zone by ID.
     * @param id the zone ID
     * @return the zone response
     */
    ZoneResponseDTO getZone(Long id);

    /**
     * Updates an existing zone's thresholds.
     * @param id the zone ID
     * @param request the update data
     * @return the updated zone response
     */
    ZoneResponseDTO updateZone(Long id, ZoneRequestDTO request);

    /**
     * Deletes a zone from the database.
     * @param id the zone ID
     */
    void deleteZone(Long id);

    /**
     * Lists all zones.
     * @return list of zone responses
     */
    List<ZoneResponseDTO> getAllZones();
}
