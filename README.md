# 🌿 EcoSync-AGMS: Polyglot Agriculture Management System

> [!IMPORTANT]
> **Status: Work In Progress (Ongoing Project)**
> This project is currently under active development. Features and documentation are subject to change.

EcoSync-AGMS is a modern, microservices-based Smart Agriculture System. It leverages multiple programming languages (Java, Node.js, Python) to handle real-time sensor telemetry, automated decision-making, and crop lifecycle management.

🏗️ System Architecture
The system is built on a Decoupled Microservices Architecture, ensuring high scalability and fault tolerance.

1. Infrastructure Services
   Service Discovery: Netflix Eureka (Java) - Central registry for all services.

API Gateway: Spring Cloud Gateway (Java) - The entry point for all client requests.

2. Domain Services
   Zone Management (Java/Spring Boot): Manages farm zones and threshold configurations (Min/Max Temp).

Sensor Telemetry (Node.js/Express): Acts as a Data Bridge, fetching real-time IoT data every 10 seconds.

Automation & Control (Python/FastAPI): The "Brain" that processes sensor data and triggers environmental controls.

Crop Inventory (Java/Spring Boot): Manages seedling-to-harvest lifecycles.

🛠️ Tech Stack

| Service | Technology | Database |
| :--- | :--- | :--- |
| **Zone Management** | Java / Spring Boot | PostgreSQL |
| **Sensor Telemetry** | Node.js / Express | MongoDB |
| **Automation & Control** | Python / FastAPI | MySQL |
| **Crop Inventory** | Java / Spring Boot | PostgreSQL |
| **Service Discovery** | Netflix Eureka | - |
| **API Gateway** | Spring Cloud Gateway | - |

🚀 Getting Started (Setup Instructions)
1. Prerequisites
   Ensure you have the following installed:

Java 17+ & Maven

Node.js 20+

Python 3.10+

Databases: PostgreSQL, MongoDB, and MySQL running locally.

2. Infrastructure Setup
   Eureka Server: Navigate to infrastructure-services/eureka-server and run `./mvnw spring-boot:run`.

API Gateway: Navigate to infrastructure-services/api-gateway and run `./mvnw spring-boot:run`.

3. Domain Services Setup
   - **Zone Management**: Navigate to `domain-services/zone-management-service` and run `./mvnw spring-boot:run`.
   - **Crop Inventory**: Navigate to `domain-services/crop-inventory-service` and run `./mvnw spring-boot:run`.
   - **Sensor Telemetry**: Navigate to `domain-services/sensor-telemetry-service`, run `npm install` (or `pnpm install`), and then `node index.js`.
   - **Automation & Control**: Navigate to `domain-services/automation-service`, create a virtual environment, run `pip install -r requirements.txt`, and then `python main.py`.

📁 Project Structure
```text
EcoSync-AGMS/
├── domain-services/
│   ├── automation-service/          # Python/FastAPI (The "Brain")
│   ├── crop-inventory-service/      # Java/Spring Boot (Lifecycle)
│   ├── sensor-telemetry-service/    # Node.js/Express (IoT Bridge)
│   └── zone-management-service/     # Java/Spring Boot (Config)
├── infrastructure-services/
│   ├── api-gateway/                 # Spring Cloud Gateway
│   └── eureka-server/               # Netflix Eureka
└── README.md
```

🤝 Contributing
This is an ongoing project. Feel free to reach out if you have suggestions for the polyglot architecture!
