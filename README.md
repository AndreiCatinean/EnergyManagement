
# Energy Management System

This application provides a robust platform for managing users, devices, and energy consumption, featuring microservices for monitoring and communication. Below are instructions and comparisons for setting up the system locally and using Docker.

---

## Table of Contents
- [Local Setup](#local-setup)
- [Docker Setup](#docker-setup)
- [Comparison: Local vs Docker](#comparison-local-vs-docker)


---

## Local Setup

### Prerequisites
- **Node.js** (Frontend: Next.js)
- **Java JDK 21** (Backend: Spring Boot)
- **PostgreSQL** (Databases for User, Device, Monitoring and Chat Microservices )
- **RabbitMQ**
- **Java** (for the Device Producer).

### Steps

1. Clone the repository:
   ```bash
   git clone https://gitlab.com/ds2024_30643_catinean_andrei/ds2024_30643_catinean_andrei_assignment2.git
   cd 
   ```

2. Install dependencies for the frontend:
   ```bash
   cd frontend
   npm install
   ```

3. Start the frontend locally:
   ```bash
   npm run dev
   ```

4. Build and run the backend services (User, Device, Monitoring):
   - Import the services as separate **Spring Boot projects** into your IDE.
   - Run each service by executing:
     ```bash
     mvn spring-boot:run
     ```

5. Configure and start PostgreSQL for each database:
   - User DB: `localhost:5434`
   - Device DB: `localhost:5435`
   - Monitoring DB: `localhost:5436`
   - Chat DB: `localhost:5437`


6. Start RabbitMQ (or another message broker):
   ```bash
   Pull the docker image and run it
   ```


## Docker Setup

### Prerequisites
- **Docker** installed on your machine.
- **Docker Compose** (optional but recommended).

### Steps

1. Clone the repository:
   ```bash
   git clone https://gitlab.com/ds2024_30643_catinean_andrei/ds2024_30643_catinean_andrei_assignment2.git
   cd <repo>
   ```

2. Build the Docker images:
   ```bash
   docker-compose build
   ```

3. Start all services:
   ```bash
   docker-compose up
   ```

4. Access the application:
   - Frontend: [http://localhost/]  -through traefik

5. Stop the containers:
   ```bash
   docker-compose down
   ```

---

## Comparison: Local vs Docker

| **Feature**                | **Local Setup**                                      | **Docker Setup**                                     |
|----------------------------|-----------------------------------------------------|-------------------------------------------------------|
| **Ease of Setup**           | Manual installation of dependencies.                | Automated setup via `docker-compose`.                |
| **Environment Consistency** | May vary depending on local OS and configurations.  | Consistent across all machines with Docker support.  |
| **Performance**             | Depends on the local machine setup.                 | Slight overhead due to containerization.             |
| **Scalability**             | Limited by manual service management.               | Scalable through traefik                             |


