package lk.ijse.zonemanagementservice.client;

import lk.ijse.zonemanagementservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * OpenFeign Client for the external IoT Provider API.
 */
@FeignClient(name = "iot-provider-client", url = "${iot.provider.url:http://104.211.95.241:8080/api}")
public interface IoTProviderClient {

    /**
     * Handshake step 1: Login to get a JWT.
     */
    @PostMapping("/auth/login")
    IoTAuthResponseDTO login(@RequestBody IoTAuthRequestDTO authRequest);

    /**
     * Handshake step 2: Register device using the JWT.
     */
    @PostMapping("/devices")
    IoTDeviceResponseDTO registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody IoTDeviceRequestDTO deviceRequest
    );

    /**
     * Get all devices registered in the IoT service.
     */
    @GetMapping("/devices")
    List<IoTDeviceResponseDTO> getAllDevices(@RequestHeader("Authorization") String token);

    /**
     * Get live telemetry for a specific device.
     */
    @GetMapping("/devices/telemetry/{deviceId}")
    Object getTelemetry(
            @RequestHeader("Authorization") String token,
            @PathVariable("deviceId") String deviceId
    );
}
