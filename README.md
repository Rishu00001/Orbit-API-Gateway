# 🌌 Orbit Smart API Gateway

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen.svg)
![Spring Security](https://img.shields.io/badge/Spring_Security-OAuth2-success)
![Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Streaming-black)
![Redis](https://img.shields.io/badge/Redis-Rate_Limiting-red)

An enterprise-grade, custom API Gateway engineered for high performance and distributed backend environments. Orbit acts as a secure reverse proxy for microservices, guaranteeing optimal system scalability by maintaining a strict, clear separation between the synchronous request-processing path and asynchronous background telemetry tasks.

## 📐 System Architecture & Working

The architecture utilizes a highly decoupled, event-driven microservices ecosystem designed for high availability and zero-latency operations.

1. **The Control Plane (Event-Driven Sync):** Administrators use the React UI to define routing rules and access policies. The **API Management Service** stores these in **MySQL** and publishes configuration update events to **Apache Kafka**. 
2. **The Data Plane (Client Flow):** Incoming requests are intercepted by **Spring Security** (OAuth2). The **Smart Gateway Service** continuously listens to Kafka to dynamically sync routes. It checks **Redis** for rate-limiting and proxies authorized requests to the **Target Server**.
3. **The Telemetry Pipeline (Background Flow):** To ensure zero-latency for the client, the Gateway asynchronously publishes access events back to **Apache Kafka**. The decoupled **Analytics Service** consumes these events and persists them into **MongoDB**.

```mermaid
graph TD
    %% Styling Definitions
    classDef core fill:#2b3440,stroke:#61afef,stroke-width:2px,color:#fff;
    classDef db fill:#3e4451,stroke:#98c379,stroke-width:2px,color:#fff;
    classDef client fill:#3e4451,stroke:#e5c07b,stroke-width:2px,color:#fff;

    %% Control Plane (Top Level)
    ReactFE["React FE"]:::client --> APIManagement["API Management Service"]:::core
    APIManagement <--> MySQL[("MySQL DB")]:::db

    %% Event-Driven Config Sync (Vertical connection)
    APIManagement -->|"Publishes Config Updates"| Kafka{"Apache Kafka"}:::db
    Kafka -->|"Listens & Syncs Routes"| SmartGateway["Smart Gateway Service"]:::core

    %% Data Plane (Middle Level)
    Client["Client (Postman / REACT)"]:::client --> SmartGateway
    SmartGateway <--> Redis[("Redis")]:::db
    SmartGateway --> Target["Target Server"]:::db

    %% Telemetry Pipeline (Bottom Level)
    SmartGateway -->|"Publishes Access Events"| Kafka
    Kafka -->|"Consumes Events"| Analytics["Analytics Service"]:::core
    Analytics <--> Mongo[("MongoDB")]:::db
```
## Core Features
```mermaid
graph LR
    Root["🚀 Orbit API Gateway"]
    
    Root --> Sec["🔒 Security"]
    Sec --> S1("Spring Security")
    Sec --> S2("OAuth2 Authorization")
    Sec --> S3("AOP Auditing via AspectJ")

    Root --> Traf["🚦 Traffic Control"]
    Traf --> T1("Redis Rate Limiting")
    Traf --> T2("Route-Level Timeouts")

    Root --> Rout["🔀 Routing & Proxy"]
    Rout --> R1("Dynamic Reverse Proxy")
    Rout --> R2("Header & Path Transformation")

    Root --> Res["🛡️ Resilience"]
    Res --> Re1("Idempotent POST")
    Res --> Re2("Global Exception Handling")

    Root --> Obs["👁️ Telemetry & Storage"]
    Obs --> O1("Kafka Event Publishing")
    Obs --> O2("MongoDB Analytics")
    Obs --> O3("MySQL Configurations")
