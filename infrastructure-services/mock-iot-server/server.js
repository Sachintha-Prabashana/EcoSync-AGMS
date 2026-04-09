const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 9090;

app.use(cors());
app.use(bodyParser.json());

// Mock Data Storage
const devices = [
    { deviceId: 'DEV-MOCK-001', name: 'Greenhouse Alpha Sensor', zoneId: 'Zone-1' },
    { deviceId: 'DEV-MOCK-002', name: 'Greenhouse Beta Sensor', zoneId: 'Zone-2' }
];

// 1. Auth Login
app.post('/api/auth/login', (req, res) => {
    const { username, password } = req.body;
    console.log(`Login attempt for user: ${username}`);
    
    if (username === 'sachintha_p' && password === '12345678') {
        return res.json({ token: 'mock-jwt-token-sachintha' });
    }
    
    return res.status(401).json({ message: 'Invalid credentials' });
});

// 2. Register Device
app.post('/api/devices', (req, res) => {
    const { name, zoneId } = req.body;
    const newDeviceId = `DEV-MOCK-${Math.floor(Math.random() * 1000)}`;
    const newDevice = { deviceId: newDeviceId, name, zoneId };
    
    devices.push(newDevice);
    console.log(`Registered new device: ${newDeviceId} for zone: ${zoneId}`);
    
    res.status(201).json(newDevice);
});

// 3. Get All Devices
app.get('/api/devices', (req, res) => {
    res.json(devices);
});

// 4. Get Telemetry for device
app.get('/api/devices/telemetry/:deviceId', (req, res) => {
    const { deviceId } = req.params;
    
    // Generate random temperature reading
    const temperature = (Math.random() * (30 - 18) + 18).toFixed(2);
    const humidity = (Math.random() * (80 - 40) + 40).toFixed(2);
    
    console.log(`Telemetry requested for ${deviceId}: ${temperature}°C`);
    
    res.json({
        temperature: parseFloat(temperature),
        humidity: parseFloat(humidity),
        status: 'ACTIVE'
    });
});

app.listen(PORT, () => {
    console.log(`Mock IoT Server listening at http://localhost:${PORT}`);
    console.log(`Base API URL: http://localhost:${PORT}/api`);
});
