package lk.ijse.cropinventoryservice.entity;

import jakarta.persistence.*;
import lombok. AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crop")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchName;
    private String cropType;
    @Enumerated(EnumType.STRING)
    private CropStatus status;
    private LocalDate plantedDate;
}
