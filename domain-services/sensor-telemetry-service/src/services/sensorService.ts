import axios from 'axios';
import cron from 'node-cron';
import Telemetry from '../models/Telemetry.js';
import type { AppConfig } from '../config/remoteConfig.js';

class SensorService {
  private config: AppConfig | null = null;

  public init(config: AppConfig) {
    this.config = config;
    this.startPolling();
  }

  private startPolling() {
    // Run every 10 seconds
    cron.schedule('*/10 * * * * *', async () => {
      console.log('--- Telemetry Fetcher Started ---');
      const config = this.config;
      if (!config) {
        console.warn('SensorService not initialized with config yet.');
        return;
      }

      try {
        // 1. Login to get Bearer Token
        const authResponse = await axios.post(`${config.iotApiUrl}/auth/login`, {
          username: config.iotUsername,
          password: config.iotPassword
        });
        const token = `Bearer ${authResponse.data.token}`;

        // 2. Get All Devices
        const devicesResponse = await axios.get(`${config.iotApiUrl}/devices`, {
          headers: { Authorization: token }
        });
        const devices = devicesResponse.data;

        // 3. Fetch Telemetry for each device
        const telemetryPromises = devices.map(async (device: any) => {
          try {
            const telemetryResponse = await axios.get(`${config.iotApiUrl}/devices/telemetry/${device.deviceId}`, {
              headers: { Authorization: token }
            });
            
            const reading = {
                deviceId: device.deviceId,
                name: device.name,
                zoneId: device.zoneId,
                data: telemetryResponse.data,
                timestamp: new Date()
            };

            // 4. Persistence: Save to MongoDB
            await Telemetry.create(reading);
            
            return reading;
          } catch (err: any) {
            console.error(`Failed to fetch telemetry for device ${device.deviceId}: ${err.message}`);
            return null;
          }
        });

        const readings = (await Promise.all(telemetryPromises)).filter(r => r !== null);
        
        if (readings.length > 0) {
            // 5. Pusher: Send latest batch to Automation Service
            await axios.post(config.automationServiceUrl, {
                timestamp: new Date().toISOString(),
                readings: readings
            });
            console.log(`Successfully pushed ${readings.length} readings to Automation Service.`);
        }

      } catch (error: any) {
        console.error('Data Bridge Error:', error.message);
      }
    });
  }

  public async getLatestReading() {
    // Fetch the absolute latest record from the DB
    return await Telemetry.findOne().sort({ timestamp: -1 });
  }
}

export const sensorService = new SensorService();
