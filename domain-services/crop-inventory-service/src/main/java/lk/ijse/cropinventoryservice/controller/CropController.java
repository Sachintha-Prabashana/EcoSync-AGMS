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
    public ResponseEntity<String> registerCrop(@RequestBody CropDTO cropDTO) {
        cropService.registerCrop(cropDTO);
        return new ResponseEntity<>("Crop registered successfully with batch: " + cropDTO.getBatchName(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateCropStatus(@PathVariable Long id, @RequestParam CropStatus status) {
        cropService.updateCropStatus(id, status);
        return new ResponseEntity<>("Crop status updated to " + status + " for ID: " + id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CropDTO> getCropById(@PathVariable Long id) {
        return new ResponseEntity<>(cropService.getCropById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCrop(@PathVariable Long id, @RequestBody CropDTO cropDTO) {
        cropService.updateCrop(id, cropDTO);
        return new ResponseEntity<>("Crop metadata updated successfully for ID: " + id, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return new ResponseEntity<>("Crop deleted successfully with ID: " + id, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CropDTO>> getAllCrops() {
        return new ResponseEntity<>(cropService.getAllCrops(), HttpStatus.OK);
    }
}
