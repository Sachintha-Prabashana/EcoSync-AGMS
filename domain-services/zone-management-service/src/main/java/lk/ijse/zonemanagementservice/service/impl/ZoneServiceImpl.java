package lk.ijse.zonemanagementservice.service.impl;

import lk.ijse.zonemanagementservice.client.IoTProviderClient;
import lk.ijse.zonemanagementservice.dto.*;
import lk.ijse.zonemanagementservice.entity.Zone;
import lk.ijse.zonemanagementservice.repository.ZoneRepository;
import lk.ijse.zonemanagementservice.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ZoneService.
 * Handles environmental limit validation and the external IoT handshake.
 */
@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final IoTProviderClient ioTProviderClient;
    private final ModelMapper modelMapper;

    @Value("${iot.provider.username:sachintha_p}")
    private String iotUsername;

    @Value("${iot.provider.password:12345678}")
    private String iotPassword;

    @Override
    @Transactional
    public ZoneResponseDTO createZone(ZoneRequestDTO request) {
        // Validation: minTemp strictly less than maxTemp
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new IllegalArgumentException("Min Temperature must be less than Max Temperature.");
        }

        // 1. Initial save to generate primary key for zoneId
        Zone zone = modelMapper.map(request, Zone.class);
        Zone savedZone = zoneRepository.save(zone);

        // 2. Perform IoT Handshake with the generated ID
        String remoteZoneId = "Zone-" + savedZone.getId();
        String deviceId = performIoTHandshake(request.getName(), remoteZoneId);

        // 3. Update with the linked deviceId
        savedZone.setDeviceId(deviceId);
        Zone finalZone = zoneRepository.save(savedZone);
        
        return mapToResponse(finalZone);
    }

    @Override
    public ZoneResponseDTO getZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));
        return mapToResponse(zone);
    }

    @Override
    @Transactional
    public ZoneResponseDTO updateZone(Long id, ZoneRequestDTO request) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));

        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new IllegalArgumentException("Min Temperature must be less than Max Temperature.");
        }

        modelMapper.map(request, zone);
        Zone updatedZone = zoneRepository.save(zone);
        return mapToResponse(updatedZone);
    }

    @Override
    @Transactional
    public void deleteZone(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new RuntimeException("Zone not found with ID: " + id);
        }
        zoneRepository.deleteById(id);
    }

    @Override
    public List<ZoneResponseDTO> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Performs the 2-step updated handshake:
     * 1. Login
     * 2. Register Device
     */
    private String performIoTHandshake(String name, String zoneId) {
        try {
            // 1. Login (Get Token)
            IoTAuthResponseDTO authResponse = ioTProviderClient.login(new IoTAuthRequestDTO(iotUsername, iotPassword));
            String token = "Bearer " + authResponse.getToken();

            // 2. Register Device with new payload
            IoTDeviceRequestDTO deviceRequest = new IoTDeviceRequestDTO(name, zoneId);
            IoTDeviceResponseDTO deviceResponse = ioTProviderClient.registerDevice(token, deviceRequest);
            
            return deviceResponse.getDeviceId();

        } catch (Exception e) {
            throw new RuntimeException("External IoT Provider Handshake Failed: " + e.getMessage(), e);
        }
    }

    private ZoneResponseDTO mapToResponse(Zone zone) {
        return modelMapper.map(zone, ZoneResponseDTO.class);
    }
}
