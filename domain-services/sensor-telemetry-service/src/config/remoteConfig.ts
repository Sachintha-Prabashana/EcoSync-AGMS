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
    
    return {
        port: parseInt(process.env.PORT || '8082'),
        iotApiUrl: remote.iotApiUrl || process.env.IOT_API_URL || '',
        iotUsername: remote.iotUsername || process.env.IOT_USERNAME || '',
        iotPassword: remote.iotPassword || process.env.IOT_PASSWORD || '',
        automationServiceUrl: remote.automationServiceUrl || process.env.AUTOMATION_SERVICE_URL || '',
        mongoUri: remote.mongoUri || process.env.MONGO_URI || 'mongodb://localhost:27017/ecosync_sensor_db'
    };
};
