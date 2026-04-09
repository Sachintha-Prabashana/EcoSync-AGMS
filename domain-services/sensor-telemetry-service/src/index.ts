import express from 'express';
import { getFullConfig } from './config/remoteConfig.js';
import { connectDB } from './config/database.js';
import { sensorService } from './services/sensorService.js';
import sensorRoutes from './routes/sensorRoutes.js';
import { registerWithEureka } from './utils/eurekaHelper.js';

const bootstrap = async () => {
    try {
        // 1. Fetch Remote Config from Spring Cloud Config Server
        const config = await getFullConfig();
        console.log('--- Configuration Loaded ---');

        // 2. Connect to MongoDB
        await connectDB(config.mongoUri);

        const app = express();
        app.use(express.json());

        // 3. Setup Routes
        app.use('/api/sensors', sensorRoutes);

        // 4. Initialize Polling Service (cron task)
        sensorService.init(config);

        // 5. Start Express Server
        app.listen(config.port, () => {
            console.log(`Sensor Telemetry Service is running on port ${config.port}`);
            
            // 6. Register with Eureka for microservice discovery
            registerWithEureka('sensor-service', config.port);
        });

    } catch (error: any) {
        console.error('Fatal Bootstrap Error:', error.message);
        process.exit(1);
    }
};

bootstrap();
