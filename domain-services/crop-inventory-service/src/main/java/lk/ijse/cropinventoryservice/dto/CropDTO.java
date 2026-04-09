package lk.ijse.cropinventoryservice.dto;

import lk.ijse.cropinventoryservice.entity.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropDTO {
    private Long id;
    private String batchName;
    private String cropType;
    private CropStatus status;
    private LocalDate plantedDate;
}
