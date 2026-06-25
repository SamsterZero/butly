# 🧑‍💻 Contributing & Local Development Setup

This project uses Docker and VS Code Dev Containers for a consistent development environment.

---

## 🚀 Recommended Setup (Dev Containers)

### Requirements
- Visual Studio Code
- Docker Desktop
- Dev Containers extension (ms-vscode-remote.remote-containers)

---

## 🐳 Open in Dev Container

Open the project in VS Code and run:

Dev Containers: Reopen in Container

This will:
- Build the development environment
- Start PostgreSQL and Redis (if configured)
- Mount the project inside the container
- Set up Java 25 and Maven

---

## ⚙️ Docker Desktop (Windows Users)

Ensure WSL integration is enabled:

Docker Desktop →
Settings →
Resources →
WSL Integration →
Enable integration with default WSL distro

This ensures:
- Proper volume mounting
- Better performance
- Smooth Dev Container experience

---

## 🔁 Manual Setup (Without Dev Containers)

docker-compose up -d
./mvnw spring-boot:run

---

## 💡 Notes

- PostgreSQL is the source of truth
- Redis is used only for caching
- Dev Containers ensure everyone has the same environment
