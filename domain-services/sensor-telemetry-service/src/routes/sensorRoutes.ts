import { Router } from 'express';
import { getLatestReading, healthCheck } from '../controllers/sensorController.js';

const router: Router = Router();

// Test endpoint to verify the service status
router.get('/health', healthCheck);

// Endpoint for debug view of the last fetched reading from MongoDB
router.get('/latest', getLatestReading);

export default router;
