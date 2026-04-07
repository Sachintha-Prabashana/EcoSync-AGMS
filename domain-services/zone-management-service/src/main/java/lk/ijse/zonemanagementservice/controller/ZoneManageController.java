package lk.ijse.zonemanagementservice.controller;

import lk.ijse.zonemanagementservice.dto.ZoneRequestDTO;
import lk.ijse.zonemanagementservice.dto.ZoneResponseDTO;
import lk.ijse.zonemanagementservice.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing Greenhouse Zones.
 * All traffic is routed via the API Gateway to /api/zones.
 */
@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneManageController {

    private final ZoneService zoneService;

    /**
     * Creation endpoint: Requires validation and IoT Handshake.
     */
    @PostMapping
    public ResponseEntity<ZoneResponseDTO> createZone(@RequestBody ZoneRequestDTO request) {
        return new ResponseEntity<>(zoneService.createZone(request), HttpStatus.CREATED);
    }

    /**
     * Single Fetch endpoint: Retrieves thresholds and linked deviceId.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponseDTO> getZone(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getZone(id));
    }

    /**
     * Threshold update endpoint for an existing zone.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponseDTO> updateZone(@PathVariable Long id, @RequestBody ZoneRequestDTO request) {
        return ResponseEntity.ok(zoneService.updateZone(id, request));
    }

    /**
     * Deletion endpoint.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all zones.
     */
    @GetMapping
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }
}
