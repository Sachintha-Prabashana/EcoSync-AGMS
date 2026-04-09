package lk.ijse.cropinventoryservice.controller;

import lk.ijse.cropinventoryservice.dto.CropDTO;
import lk.ijse.cropinventoryservice.entity.CropStatus;
import lk.ijse.cropinventoryservice.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @PostMapping
    public ResponseEntity<Void> registerCrop(@RequestBody CropDTO cropDTO) {
        cropService.registerCrop(cropDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCropStatus(@PathVariable Long id, @RequestParam CropStatus status) {
        cropService.updateCropStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CropDTO>> getAllCrops() {
        return new ResponseEntity<>(cropService.getAllCrops(), HttpStatus.OK);
    }
}
