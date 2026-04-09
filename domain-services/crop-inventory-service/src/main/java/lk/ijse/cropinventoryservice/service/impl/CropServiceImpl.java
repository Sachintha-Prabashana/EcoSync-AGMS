package lk.ijse.cropinventoryservice.service.impl;

import lk.ijse.cropinventoryservice.dto.CropDTO;
import lk.ijse.cropinventoryservice.entity.Crop;
import lk.ijse.cropinventoryservice.entity.CropStatus;
import lk.ijse.cropinventoryservice.repository.CropRepository;
import lk.ijse.cropinventoryservice.service.CropService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;

    @Override
    public void registerCrop(CropDTO dto) {
        Crop crop = modelMapper.map(dto, Crop.class);
        cropRepository.save(crop);
    }

    @Override
    public void updateCropStatus(Long id, CropStatus newStatus) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found with id: " + id));

        CropStatus currentStatus = crop.getStatus();

        if (currentStatus == newStatus) {
            return; // Idempotent
        }

        boolean isValidTransition = false;

        if (currentStatus == CropStatus.SEEDLING && newStatus == CropStatus.VEGETATIVE) {
            isValidTransition = true;
        } else if (currentStatus == CropStatus.VEGETATIVE && newStatus == CropStatus.HARVESTED) {
            isValidTransition = true;
        }

        if (!isValidTransition) {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        crop.setStatus(newStatus);
        cropRepository.save(crop);
    }

    @Override
    public CropDTO getCropById(Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found with id: " + id));
        return modelMapper.map(crop, CropDTO.class);
    }

    @Override
    public void updateCrop(Long id, CropDTO dto) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found with id: " + id));
        
        // Update metadata fields only (status has its own dedicated method)
        crop.setBatchName(dto.getBatchName());
        crop.setCropType(dto.getCropType());
        crop.setPlantedDate(dto.getPlantedDate());
        
        cropRepository.save(crop);
    }

    @Override
    public void deleteCrop(Long id) {
        if (!cropRepository.existsById(id)) {
            throw new IllegalArgumentException("Crop not found with id: " + id);
        }
        cropRepository.deleteById(id);
    }

    @Override
    public List<CropDTO> getAllCrops() {
        return cropRepository.findAll().stream()
                .map(crop -> modelMapper.map(crop, CropDTO.class))
                .collect(Collectors.toList());
    }
}
