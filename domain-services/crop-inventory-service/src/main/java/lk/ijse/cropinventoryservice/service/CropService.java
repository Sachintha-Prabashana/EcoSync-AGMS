package lk.ijse.cropinventoryservice.service;

import lk.ijse.cropinventoryservice.dto.CropDTO;
import lk.ijse.cropinventoryservice.entity.CropStatus;

import java.util.List;

public interface CropService {
    void registerCrop(CropDTO dto);
    void updateCropStatus(Long id, CropStatus newStatus);
    CropDTO getCropById(Long id);
    void updateCrop(Long id, CropDTO dto);
    void deleteCrop(Long id);
    List<CropDTO> getAllCrops();
}
