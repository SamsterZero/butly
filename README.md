# Butly 🔗

> Butly is a URL shortener service.
> "Butly" is slang for *short*, representing compact URLs.

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![GHCR](https://img.shields.io/badge/GHCR-container-blue)

---
## 🧑‍💻 Development Setup

This project is optimized for Docker + VS Code Dev Containers.

👉 See CONTRIBUTING.md for full setup instructions.

---

## 📑 Table of Contents

- [Tech Stack](#-tech-stack)
- [Features](#️-features)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Run with Docker](#-run-with-docker-recommended)
- [API](#-api)
- [Redis Key Schema](#-redis-key-schema)
- [Environment Variables](#-environment-variables)
- [Notes](#-notes)
- [License](#-license)

---

## 🚀 Tech Stack

| Component | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.x |
| Database | PostgreSQL |
| Cache | Redis |
| ORM | JPA (Hibernate) |
| Containerization | Docker / Docker Compose |

---

## ⚙️ Features

- Shorten long URLs into compact, shareable links
- Redirect from a short URL to the original long URL
- Base62-based ID encoding for short, URL-safe codes
- Redis caching for low-latency redirects
- PostgreSQL persistence as the source of truth

---

## 🧠 Architecture

```
Client → Spring Boot → Redis → PostgreSQL
```

- **Redis** — fast path for redirects (cache layer)
- **PostgreSQL** — source of truth for all shortened URLs

**Flow:**
1. Client requests a short URL for a long URL → persisted in PostgreSQL → cached in Redis.
2. Client hits the short URL → service checks Redis first → falls back to PostgreSQL on a cache miss → repopulates Redis.

---

## 🏁 Getting Started

### Prerequisites

- Java 25
- Docker & Docker Compose
- Maven (or use the included wrapper `./mvnw`)

### Run with Docker

```bash
docker run -p 8080:8080 ghcr.io/samsterzero/butly:latest
```

The service will be available at `http://localhost:8080` (adjust if you've changed the port).

### 🧩 Run full stack (App + Postgres + Redis)

```yaml
services:
  app:
    image: ghcr.io/samsterzero/butly:latest
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/butly
      SPRING_DATASOURCE_USERNAME: butly
      SPRING_DATASOURCE_PASSWORD: change_me
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379

  postgres:
    image: postgres:18
    environment:
      POSTGRES_DB: butly
      POSTGRES_USER: butly
      POSTGRES_PASSWORD: change_me

  redis:
    image: redis:8-alpine
```

**Run:**
```bash
docker compose up -d
```

### Run locally (without Docker)

```bash
# Start only PostgreSQL and Redis via Docker
docker-compose up -d postgres redis

# Run the app
./mvnw spring-boot:run
```

---

## 🌐 API

### Create Short URL

`POST /api/urls`

**Request**
```json
{
  "longUrl": "https://example.com"
}
```

**Response — `201 Created`**
```json
{
  "shortUrl": "abc123xy"
}
```

**Error Response — `400 Bad Request`**
```json
{
  "error": "Invalid URL format"
}
```

### Redirect to Long URL

`GET /{shortCode}`

**Behavior:**
1. Checks Redis for `shortUrl:{code}`.
2. On a cache miss, falls back to PostgreSQL and repopulates Redis.
3. Returns a `302 Found` redirect to the original long URL.

**Example**
```bash
curl -i http://localhost:8080/abc123xy
```

**Response**
```
HTTP/1.1 302 Found
Location: https://example.com
```

**Error Response — `404 Not Found`** (unknown short code)
```json
{
  "error": "Short URL not found"
}
```

---

## 🔴 Redis Key Schema

```
Key:   shortUrl:{code}
Value: {longUrl}
TTL:   24 hours
```

After the TTL expires, the entry is evicted from Redis but remains permanently in PostgreSQL. The next redirect request for that code will repopulate the cache.

---

## 📦 Environment Variables

Create a `.env.docker` file (see `.env.docker.example` for a template — **never commit real credentials**):

```env
POSTGRES_USER=butly
POSTGRES_PASSWORD=change_me
POSTGRES_DB=butly
POSTGRES_PORT=5432
REDIS_PORT=6379
APP_PORT=8080
```

> ⚠️ **Security note:** Always use a `.env.docker.example` file with placeholder values for version control, and keep your real `.env.docker` in `.gitignore`.

---

## 🧩 Notes

- Base62 encoding is used to generate short, URL-safe codes.
- The database-generated ID is the source of uniqueness for each short code.
- Redis exists purely for performance — PostgreSQL remains authoritative if the cache is ever cleared or unavailable.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
