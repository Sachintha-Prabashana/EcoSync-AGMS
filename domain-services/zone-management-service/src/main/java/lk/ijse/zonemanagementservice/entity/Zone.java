package lk.ijse.zonemanagementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double minTemp;

    @Column(nullable = false)
    private Double maxTemp;

    @Column(nullable = false)
    private Double minHumidity;

    @Column(nullable = false)
    private Double maxHumidity;

    /**
     * The device ID linked to this zone, returned by the external IoT provider
     * after a successful device registration handshake.
     */
    @Column(name = "device_id")
    private String deviceId;
}
