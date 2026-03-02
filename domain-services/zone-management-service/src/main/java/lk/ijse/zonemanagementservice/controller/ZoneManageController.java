package lk.ijse.zonemanagementservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/zones")
public class ZoneManageController {

    @GetMapping
    public ResponseEntity<String> checkZoneManagement() {
        return new ResponseEntity<>("Zone Management Service is up and running!", HttpStatusCode.valueOf(200));
    }
}
