import type { Request, Response } from 'express';
import { sensorService } from '../services/sensorService.js';

export const getLatestReading = async (req: Request, res: Response) => {
  try {
    const latest = await sensorService.getLatestReading();
    if (!latest) {
      return res.status(404).json({ message: 'No telemetry data found in MongoDB.' });
    }
    res.json(latest);
  } catch (error: any) {
    res.status(500).json({ message: 'Internal Server Error', error: error.message });
  }
};
export const healthCheck = async (req: Request, res: Response) => {
  res.json({
    status: 'UP',
    service: 'Sensor Telemetry Service',
    timestamp: new Date().toISOString()
  });
};
