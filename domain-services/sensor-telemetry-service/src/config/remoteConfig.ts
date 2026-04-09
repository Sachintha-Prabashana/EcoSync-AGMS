import axios from 'axios';
import dotenv from 'dotenv';

dotenv.config();

export interface AppConfig {
    port: number;
    iotApiUrl: string;
    iotUsername: string;
    iotPassword: string;
    automationServiceUrl: string;
    mongoUri: string;
}

export const fetchRemoteConfig = async (): Promise<Partial<AppConfig>> => {
    const configServerUrl = process.env.CONFIG_SERVER_URL || 'http://localhost:8888';
    const appName = 'sensor-service';
    const profile = 'default';

    try {
        console.log(`Fetching remote configuration from ${configServerUrl}/${appName}/${profile}`);
        const response = await axios.get(`${configServerUrl}/${appName}/${profile}`);
        
        const remoteConfig: any = {};
        
        // Spring Cloud Config returns propertySources. We iterate to flatten them.
        if (response.data && response.data.propertySources) {
            response.data.propertySources.forEach((source: any) => {
                Object.assign(remoteConfig, source.source);
            });
        }

        return {
            iotApiUrl: remoteConfig['iot.provider.url'],
            iotUsername: remoteConfig['iot.provider.username'],
            iotPassword: remoteConfig['iot.provider.password'],
            automationServiceUrl: remoteConfig['automation.service.url'],
            mongoUri: remoteConfig['spring.data.mongodb.uri'] || process.env.MONGO_URI
        };
    } catch (error: any) {
        console.warn('Could not fetch remote config, falling back to local .env:', error.message);
        return {};
    }
};

export const getFullConfig = async (): Promise<AppConfig> => {
    const remote = await fetchRemoteConfig();
    
    // Priority: Local .env > Remote Config Server
    const config = {
        port: parseInt(process.env.PORT || '8082'),
        iotApiUrl: process.env.IOT_API_URL || remote.iotApiUrl || '',
        iotUsername: process.env.IOT_USERNAME || remote.iotUsername || 'sachintha_p',
        iotPassword: process.env.IOT_PASSWORD || remote.iotPassword || '12345678',
        automationServiceUrl: process.env.AUTOMATION_SERVICE_URL || remote.automationServiceUrl || 'http://localhost:8083/api/v1/automation/process',
        mongoUri: process.env.MONGO_URI || remote.mongoUri || 'mongodb://localhost:27017/ecosync_sensor_db'
    };

    console.log(`[Config] Using IoT API URL: ${config.iotApiUrl}`);
    return config;
};
