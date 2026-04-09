<div align="center">

# 🌿 EcoSync-AGMS

### Automated Greenhouse Management System

*A production-grade, polyglot microservices platform for real-time smart agriculture*

[![Java](https://img.shields.io/badge/Java_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white)](https://nodejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Python](https://img.shields.io/badge/Python_3.10+-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org/)
[![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white)](https://fastapi.tiangolo.com/)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Service Port Map](#-service-port-map)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [Data Flow](#-data-flow)
- [Documentation](#-documentation)

---

## 🌱 Overview

**EcoSync-AGMS** is a real-time, event-driven **Smart Greenhouse Automation System** built with a **polyglot microservices architecture**. The system continuously monitors environmental conditions (temperature, humidity) through IoT sensors, evaluates them against zone-specific thresholds, and automatically triggers climate control decisions — all without manual intervention.

### ✨ Key Highlights

- 🔄 **Real-Time IoT Pipeline** — Sensor telemetry is polled every 10 seconds and pushed through the full automation chain
- 🧠 **Intelligent Automation** — Python-based decision engine evaluates readings against configurable zone thresholds
- 🌐 **Polyglot Architecture** — Java, Node.js (TypeScript), and Python services working in harmony
- ⚙️ **Centralized Config** — All services pull config from Spring Cloud Config Server backed by a Git repository
- 🔍 **Service Discovery** — Netflix Eureka enables dynamic service registration and lookup
- 🚪 **Unified Entry Point** — Spring Cloud Gateway routes all traffic with a single base URL

---

## 🏗️ Architecture

```
                           ┌─────────────────────────────┐
                           │    Spring Cloud Config       │
                           │       (Port 8888)            │
                           │  GitHub: ecosync-config-repo │
                           └──────────────┬──────────────┘
                                          │ Pulls config
          ┌───────────────────────────────┼───────────────────────────────┐
          │                              ↓                               │
          │         ┌──────────────────────────────────┐                 │
          │         │        Netflix Eureka            │                 │
          │         │     Service Discovery            │                 │
          │         │         (Port 8761)              │                 │
          │         └──────────────────────────────────┘                 │
          │                     ↑ registers                              │
          ↓                     │                                        ↓
 ┌────────────────┐   ┌─────────────────┐   ┌──────────────────┐  ┌────────────────┐
 │  Zone Mgmt     │   │ Sensor Telemetry │   │   Automation     │  │ Crop Inventory │
 │ Java/Spring    │   │  Node.js / TS    │   │ Python / FastAPI │  │  Java/Spring   │
 │  Port: 8081    │←──│  Port: 8082      │──→│  Port: 8083      │  │  Port: 8084    │
 │  PostgreSQL    │   │  MongoDB         │   │  MySQL           │  │  PostgreSQL    │
 └────────────────┘   └────────┬─────────┘   └──────────────────┘  └────────────────┘
          ↑                    │ polls every 10s
          │            ┌───────▼────────┐
          └────────────│  Mock IoT      │
      device register  │  Server        │
                       │  Node.js       │
                       │  Port: 9090    │
                       └────────────────┘
                                ↑
                        ┌───────┴────────┐
                        │  API Gateway   │
                        │ Spring Cloud   │
                        │  Port: 8080    │
                        └────────────────┘
                                ↑
                            Client Requests
```

---

## 🛠️ Tech Stack

| Service | Language | Framework | Database | Port |
|:---|:---|:---|:---|:---:|
| **API Gateway** | Java 17 | Spring Cloud Gateway | — | `8080` |
| **Eureka Server** | Java 17 | Spring Netflix Eureka | — | `8761` |
| **Config Server** | Java 17 | Spring Cloud Config | Git Repo | `8888` |
| **Zone Management** | Java 17 | Spring Boot 3 | PostgreSQL | `8081` |
| **Sensor Telemetry** | TypeScript | Node.js / Express | MongoDB | `8082` |
| **Automation & Control** | Python 3.10+ | FastAPI + SQLAlchemy | MySQL | `8083` |
| **Crop Inventory** | Java 17 | Spring Boot 3 | PostgreSQL | `8084` |
| **Mock IoT Server** | JavaScript | Node.js / Express | In-Memory | `9090` |

### Core Dependencies

| Layer | Technology |
|:---|:---|
| Service Discovery | Netflix Eureka |
| Config Management | Spring Cloud Config + GitHub |
| API Routing | Spring Cloud Gateway |
| ORM (Java) | Spring Data JPA + Hibernate |
| ORM (Python) | SQLAlchemy 2.0 |
| IoT Client (Java) | OpenFeign |
| Eureka Client (Node.js) | `eureka-js-client` |
| Eureka Client (Python) | `py_eureka_client` |

---

## 🗺️ Service Port Map

```
localhost:8080  →  API Gateway          (all client traffic goes here)
localhost:8081  →  Zone Management
localhost:8082  →  Sensor Telemetry
localhost:8083  →  Automation Service
localhost:8084  →  Crop Inventory
localhost:8761  →  Eureka Dashboard
localhost:8888  →  Config Server
localhost:9090  →  Mock IoT Server
```

---

## 📁 Project Structure

```
EcoSync-AGMS/
│
├── 📂 infrastructure-services/
│   ├── api-gateway/               # Spring Cloud Gateway — single entry point
│   ├── config-server/             # Spring Cloud Config — centralized config via Git
│   ├── eureka-server/             # Netflix Eureka — service discovery registry
│   └── mock-iot-server/           # Node.js Express — local IoT simulator (Port 9090)
│
├── 📂 domain-services/
│   ├── zone-management-service/   # Java/Spring Boot — greenhouse zones & IoT handshake
│   │   └── src/main/java/lk/ijse/zonemanagementservice/
│   │       ├── controller/        # ZoneManageController (CRUD + IoT register)
│   │       ├── service/           # ZoneService (threshold validation)
│   │       ├── dto/               # ZoneRequestDTO, ZoneResponseDTO
│   │       └── feign/             # IoT Provider OpenFeign client
│   │
│   ├── sensor-telemetry-service/  # Node.js/TypeScript — IoT data bridge
│   │   └── src/
│   │       ├── config/            # Remote config + DB config
│   │       ├── controllers/       # sensorController (health, latest)
│   │       ├── models/            # MongoDB TelemetryReading schema
│   │       ├── routes/            # /api/sensors
│   │       ├── services/          # sensorService (cron polling + push to automation)
│   │       └── utils/             # Eureka registration helper
│   │
│   ├── automation-service/        # Python/FastAPI — decision engine
│   │   └── app/
│   │       ├── api/               # routes.py — /process, /health endpoints
│   │       ├── core/              # config.py, database.py, remote_config.py
│   │       ├── models/            # AutomationLog SQLAlchemy model
│   │       └── services/          # automation_service.py (rules evaluation)
│   │
│   └── crop-inventory-service/    # Java/Spring Boot — crop lifecycle management
│       └── src/main/java/lk/ijse/cropinventoryservice/
│           ├── controller/        # CropController (CRUD + status update)
│           ├── service/           # CropService
│           ├── dto/               # CropDTO
│           └── entity/            # Crop entity, CropStatus enum
│
├── 📂 collection/
│   └── EcoSync-AGMS.postman_collection.json   # Ready-to-import Postman collection
│
├── 📂 docs/
│   └── eureka-dashboard-screenshot.png        # Eureka dashboard with all services UP
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Before starting, make sure the following are installed and running:

| Tool | Version | Purpose |
|:---|:---|:---|
| JDK | 17+ | Java services |
| Maven | 3.9+ | Build Java services (or use included `mvnw`) |
| Node.js | 20+ | Sensor Telemetry & Mock IoT |
| npm / pnpm | Latest | Node.js package management |
| Python | 3.10+ | Automation service |
| PostgreSQL | 14+ | Zone & Crop databases |
| MongoDB | 6+ | Telemetry storage |
| MySQL | 8+ | Automation decision logs |

### Database Setup

Create the required databases before starting the services:

```sql
-- PostgreSQL
CREATE DATABASE ecosync_zone_db;
CREATE DATABASE ecosync_crop_db;

-- MySQL
CREATE DATABASE ecosync_automation_db;
```

MongoDB database is created automatically on first connection.

---

### ⚠️ Startup Order

Services **must** be started in this order to avoid dependency failures:

```
1. Mock IoT Server
2. Eureka Server
3. Config Server
4. Domain Services (any order)
5. API Gateway (last)
```

---

### Step 1 — Mock IoT Server

```bash
cd infrastructure-services/mock-iot-server
npm install
node server.js
# Running at http://localhost:9090
```

### Step 2 — Eureka Server

```bash
cd infrastructure-services/eureka-server
./mvnw spring-boot:run
# Dashboard at http://localhost:8761
```

### Step 3 — Config Server

```bash
cd infrastructure-services/config-server
./mvnw spring-boot:run
# Running at http://localhost:8888
```

### Step 4 — Domain Services

Open **4 separate terminals** and run each:

```bash
# Terminal 1 — Zone Management (Java)
cd domain-services/zone-management-service
./mvnw spring-boot:run

# Terminal 2 — Crop Inventory (Java)
cd domain-services/crop-inventory-service
./mvnw spring-boot:run

# Terminal 3 — Sensor Telemetry (TypeScript)
cd domain-services/sensor-telemetry-service
pnpm install   # or: npm install
npm start

# Terminal 4 — Automation Service (Python)
cd domain-services/automation-service
python -m venv .venv
.venv\Scripts\activate        # Windows
# source .venv/bin/activate   # Linux/macOS
pip install -r requirements.txt
python main.py
```

### Step 5 — API Gateway

```bash
cd infrastructure-services/api-gateway
./mvnw spring-boot:run
# All traffic now routes through http://localhost:8080
```

---

### Environment Configuration

**Automation Service** — create `domain-services/automation-service/.env`:
```env
PORT=8083
EUREKA_SERVER=http://localhost:8761/eureka
MYSQL_URL=mysql+mysqlconnector://root:<password>@localhost:3306/ecosync_automation_db
APP_NAME=automation-service
```

**Mock IoT Server** — credentials are hardcoded in `server.js`:
```
username: sachintha_p
password: 12345678
```

---

## 📡 API Reference

All requests go through the **API Gateway** at `http://localhost:8080`.

### Zone Management — `/zone-service/api/zones`

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/zone-service/api/zones` | Create a new greenhouse zone |
| `GET` | `/zone-service/api/zones` | List all zones |
| `GET` | `/zone-service/api/zones/{id}` | Get a zone by ID |
| `PUT` | `/zone-service/api/zones/{id}` | Update zone thresholds |
| `DELETE` | `/zone-service/api/zones/{id}` | Delete a zone |

**Sample Create Zone Request:**
```json
{
    "name": "Line 01 - Orchid Hall",
    "minTemp": 18.0,
    "maxTemp": 24.0,
    "minHumidity": 50.0,
    "maxHumidity": 70.0
}
```

---

### Crop Inventory — `/crop-service/api/crops`

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/crop-service/api/crops` | Register a new crop batch |
| `GET` | `/crop-service/api/crops` | List all crop batches |
| `GET` | `/crop-service/api/crops/{id}` | Get crop details |
| `PUT` | `/crop-service/api/crops/{id}` | Update crop metadata |
| `PUT` | `/crop-service/api/crops/{id}/status?status=HARVESTED` | Update crop status |
| `DELETE` | `/crop-service/api/crops/{id}` | Delete a crop batch |

**Crop Statuses:** `PLANTED` → `SEEDLING` → `VEGETATIVE` → `FLOWERING` → `HARVESTED`

---

### Sensor Telemetry — `/sensor-service/api/sensors`

| Method | Endpoint | Description |
|:---|:---|:---|
| `GET` | `/sensor-service/api/sensors/health` | Service health check |
| `GET` | `/sensor-service/api/sensors/latest` | Latest telemetry reading from MongoDB |

---

### Automation Service — `/automation-service/api/v1/automation`

| Method | Endpoint | Description |
|:---|:---|:---|
| `GET` | `/automation-service/api/v1/automation/health` | Service health check |
| `POST` | `/automation-service/api/v1/automation/process` | Process a batch of sensor readings |

**Sample Process Request:**
```json
{
    "timestamp": "2026-04-09T10:00:00Z",
    "readings": [
        {
            "deviceId": "DEV-MOCK-001",
            "zoneId": "1",
            "temperature": 35.5,
            "humidity": 70.0
        }
    ]
}
```

---

### Mock IoT Server — `http://localhost:9090`

| Method | Endpoint | Description |
|:---|:---|:---|
| `POST` | `/api/auth/login` | Obtain mock JWT token |
| `GET` | `/api/devices` | List all registered IoT devices |
| `POST` | `/api/devices` | Register a new device |
| `GET` | `/api/devices/telemetry/{deviceId}` | Get live telemetry for a device |

---

## 🔄 Data Flow

The system runs an **automated pipeline** every 10 seconds:

```
Mock IoT Server (port 9090)
        │
        │  poll all device telemetry
        ↓
Sensor Telemetry Service (port 8082)
        │  persist to MongoDB
        │  batch readings → POST /automation/process
        ↓
Automation Service (port 8083)
        │  for each reading:
        │    1. GET zone thresholds from Zone Management
        │    2. evaluate temperature vs. min/max
        │    3. decide: COOLING_ON / HEATING_ON / NORMAL
        │  persist decision to MySQL
        ↓
AutomationLog (MySQL)
```

---

## 📚 Documentation

| Resource | Location | Description |
|:---|:---|:---|
| **Postman Collection** | [`collection/EcoSync-AGMS.postman_collection.json`](./collection/EcoSync-AGMS.postman_collection.json) | Full API test collection — import directly into Postman |
| **Eureka Dashboard** | [`docs/eureka-dashboard-screenshot.png`](./docs/eureka-dashboard-screenshot.png) | Screenshot of all services registered as UP |
| **Config Repository** | [ecosync-config-repo](https://github.com/Sachintha-Prabashana/ecosync-config-repo) | Centralized config files for all services |

### Importing the Postman Collection

1. Open Postman
2. Click **Import** → select `collection/EcoSync-AGMS.postman_collection.json`
3. Set the collection variables:
   - `baseUrl` → `http://localhost:8080`
   - `iotUrl` → `http://localhost:9090`
4. Run the **IoT Login** request first to get the mock token
5. All other requests are ready to use

---

## 🤝 Contributing

This is an active academic/portfolio project. The polyglot architecture demonstrates real-world patterns used in enterprise microservices — feel free to open issues or reach out with suggestions.

---

<div align="center">

**EcoSync-AGMS** — *Growing smarter, one microservice at a time* 🌱

</div>
