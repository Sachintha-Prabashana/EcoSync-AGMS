import express from 'express';
import { registerWithEureka } from './eureka-helper.js';

const app = express();
const PORT = process.env.PORT || 8082;

app.listen(PORT, () => {
    console.log(`Sensor Telemetry Service is running on port ${PORT}`);
    registerWithEureka();
});