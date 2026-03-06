package lk.ijse.cropinventoryservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crops")
public class CropManageController {
    @GetMapping
    public ResponseEntity<String> checkCropManagement(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
