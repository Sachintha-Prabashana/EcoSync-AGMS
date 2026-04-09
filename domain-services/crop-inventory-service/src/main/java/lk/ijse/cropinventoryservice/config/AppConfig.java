package lk.ijse.cropinventoryservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Crop Inventory Service.
 * Defines ModelMapper as a Spring Bean for automated object mapping.
 */
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
