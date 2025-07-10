# 🚀 WhatsApp Clone – Microservices Architecture

Welcome to the **WhatsApp Clone** – a full-stack, production-grade chat application built using a modern microservices architecture. Inspired by WhatsApp Web, this project showcases distributed systems concepts with real-world tech stacks and containerized deployment.

---

## 🧩 Architecture Overview

* **Microservices** – Feature-driven, independent Spring Boot services (Profile, Room, Message, Image)
* **API Gateway** – Node.js TypeScript-based unified entry point
* **Service Discovery** – Eureka server for automatic registration and lookup
* **Centralized Config** – Spring Cloud Config server for externalized configuration
* **Frontend** – Built with React + Vite for blazing-fast performance
* **Real-Time Messaging** – Powered by Socket.io
* **Database Strategy** – Independent DB per service (polyglot persistence friendly)
* **Dockerized** – Fully containerized with `docker-compose` for local orchestration
* **Cloud-Ready** – CI/CD friendly and scalable

---

## 📁 Project Structure

```
whatsapp-clone-microservices/
│
├── api_gateway/         # Node.js API Gateway (TypeScript)
├── config_server/       # Spring Cloud Config Server
├── eureka_server/       # Eureka Service Registry
├── frontend/            # Next.js frontend (React + Vite)
├── image_service/       # Handles image upload & access
├── message_service/     # Real-time chat messaging
├── profile_service/     # User profile management
├── room_service/        # Chat room creation & joining
└── docker-compose.yml   # Orchestrates all services
```

---

## ✨ Key Features

* 🔐 **User Authentication** – Signup & login with secure token-based auth
* 👤 **Profile Management** – Display name, avatar, etc.
* 💬 **Real-Time Chat** – Instant message delivery via WebSockets
* 🗂 **Room Management** – Create, join, leave group chats
* 📸 **Image Sharing** – Upload and send images in messages
* 🔎 **User Search** – Find users by name or email
* 📡 **Service Discovery** – Dynamic routing via Eureka
* ⚙️ **Central Config** – Runtime config updates through a central server
* 🛡️ **API Gateway** – Single entrypoint with routing and security policies
* 🐳 **Dockerized** – All services containerized for consistency

---

## 🛠 Tech Stack

| Layer         | Tech Stack                             |
| ------------- | -------------------------------------- |
| **Frontend**  | React, Vite, Styled Components, Axios  |
| **Gateway**   | Node.js, TypeScript, Express           |
| **Backend**   | Spring Boot (Java), Spring Cloud       |
| **Real-Time** | Socket.io                              |
| **Discovery** | Eureka                                 |
| **Config**    | Spring Cloud Config Server             |
| **Databases** | H2 (Dev), Extensible to MySQL/Postgres |
| **DevOps**    | Docker, Docker Compose                 |

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/AyushDocs/whatsapp-clone-microservices.git
cd whatsapp-clone-microservices
```

### 2. Start all services with Docker Compose

```bash
docker-compose up --build
```

### 3. Access the app

| Component        | URL                                            |
| ---------------- | ---------------------------------------------- |
| Frontend UI      | [http://localhost:3000](http://localhost:3000) |
| API Gateway      | [http://localhost:8080](http://localhost:8080) |
| Eureka Dashboard | [http://localhost:8761](http://localhost:8761) |

---

## 🖼 Screenshots


---

## 🤝 Contributing

Contributions are welcome!

* Fork the repo
* Create a new branch
* Submit a pull request
* Report bugs and suggest features via Issues tab

---

## 📝 License

MIT License © \[Ayush Dubey]

---

## 🙏 Acknowledgements

* Inspired by **WhatsApp Web**
* Built with ❤️ using **Spring Boot**, **React**, **Node.js**, and **Docker**
* Created for educational, demonstration, and scaling practice

---