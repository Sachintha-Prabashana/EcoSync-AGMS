package lk.ijse.zonemanagementservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Zone Management Service.
 * Defines shared Spring Beans.
 */
@Configuration
public class AppConfig {

    /**
     * Define ModelMapper as a Spring Bean for automated object mapping.
     * @return a new ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
