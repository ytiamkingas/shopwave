# 🛒 ShopWave — E-Commerce Microservices Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.4-green?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql" />
  <img src="https://img.shields.io/badge/MongoDB-7-green?style=for-the-badge&logo=mongodb" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" />
  <img src="https://img.shields.io/badge/JWT-Security-black?style=for-the-badge&logo=jsonwebtokens" />
</p>

A **production-ready, microservices-based e-commerce backend** built with Java 17 and Spring Boot 3. Demonstrates real-world patterns including JWT authentication, role-based authorization, dual-database architecture (PostgreSQL + MongoDB), Spring Cloud Gateway routing, and full Docker containerization.

---

## 📐 Architecture

```
                        ┌─────────────────────┐
        Client          │    API Gateway       │  :8080
      Requests  ──────► │  Spring Cloud GW     │
                        │  JWT Validation      │
                        └──────────┬──────────┘
                                   │ Routes to...
          ┌──────────┬─────────────┼──────────────┬──────────────┐
          ▼          ▼             ▼              ▼              ▼
   ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐
   │  User Svc  │ │Product Svc │ │ Order Svc  │ │  Cart Svc  │
   │   :8081    │ │   :8082    │ │   :8083    │ │   :8084    │
   │ PostgreSQL │ │  MongoDB   │ │ PostgreSQL │ │  MongoDB   │
   └────────────┘ └────────────┘ └────────────┘ └────────────┘
```

| Service | Port | Database | Responsibility |
|---------|------|----------|---------------|
| API Gateway | 8080 | — | Route requests, JWT validation |
| User Service | 8081 | PostgreSQL | Registration, Login, JWT issuance |
| Product Service | 8082 | MongoDB | Product catalog, search, categories |
| Order Service | 8083 | PostgreSQL | Place orders, order history |
| Cart Service | 8084 | MongoDB | Add/remove items, view cart |

---

## 🚀 Tech Stack

| Category | Technology |
|----------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.4 |
| API Layer | Spring MVC (REST) |
| Security | Spring Security 6 + JWT (jjwt 0.12) |
| ORM | Spring Data JPA / Hibernate |
| NoSQL ORM | Spring Data MongoDB |
| Relational DB | PostgreSQL 15 |
| Document DB | MongoDB 7 |
| API Gateway | Spring Cloud Gateway |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) |
| Build | Maven 3.9 |
| Containers | Docker + Docker Compose |

---

## ⚡ Quick Start (Docker — Recommended)

### Prerequisites
- [Docker](https://docs.docker.com/get-docker/) & Docker Compose
- Java 17+ (only for running locally without Docker)
- Maven 3.9+ (only for running locally without Docker)

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/shopwave.git
cd shopwave
```

### 2. Start everything with one command
```bash
docker compose up --build
```

Docker will automatically:
- Start PostgreSQL and MongoDB
- Run `init.sql` to create tables and seed an admin user
- Build and start all 5 microservices

### 3. Verify all services are running
```bash
docker compose ps
```

All services should show `Up` status. First startup may take 2–3 minutes while Maven downloads dependencies.

---

## 🔑 Authentication Flow

### Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login and get JWT token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "password123"}'
```

**Response:**
```json
{
  "accessToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "userId": 2,
  "email": "john@example.com",
  "role": "CUSTOMER",
  "expiresIn": 86400
}
```

### Use the token in subsequent requests
```bash
export TOKEN="eyJhbGci..."

curl http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN"
```

### Pre-seeded Admin account
```
Email:    admin@shopwave.com
Password: Admin@1234
Role:     ADMIN
```

---

## 📚 API Reference

### Auth Endpoints (no token required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |

### User Endpoints
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/users/me` | USER | Get own profile |
| PUT | `/api/users/me` | USER | Update own profile |
| GET | `/api/users` | ADMIN | List all users |
| GET | `/api/users/{id}` | ADMIN | Get user by ID |
| DELETE | `/api/users/{id}` | ADMIN | Delete user |

### Product Endpoints
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/products` | None | List products (filter: `?category=&search=&minPrice=&maxPrice=`) |
| GET | `/api/products/{id}` | None | Get product by ID |
| POST | `/api/products` | ADMIN | Create product |
| PUT | `/api/products/{id}` | ADMIN | Update product |
| DELETE | `/api/products/{id}` | ADMIN | Soft delete product |
| PATCH | `/api/products/{id}/stock` | ADMIN | Update stock |
| GET | `/api/products/low-stock` | ADMIN | Products below threshold |

### Order Endpoints
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/orders` | USER | Place new order |
| GET | `/api/orders` | USER | My order history |
| GET | `/api/orders/{id}` | USER | Get order details |
| PATCH | `/api/orders/{id}/status` | ADMIN | Update order status |
| DELETE | `/api/orders/{id}` | USER | Cancel order |
| GET | `/api/orders/status/{status}` | ADMIN | Orders by status |

### Cart Endpoints
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/cart` | USER | View cart |
| POST | `/api/cart/items` | USER | Add item to cart |
| PUT | `/api/cart/items/{productId}` | USER | Update item quantity |
| DELETE | `/api/cart/items/{productId}` | USER | Remove item |
| DELETE | `/api/cart` | USER | Clear cart |

---

## 📖 Swagger UI

Each service exposes interactive API documentation:

| Service | URL |
|---------|-----|
| User Service | http://localhost:8081/swagger-ui.html |
| Product Service | http://localhost:8082/swagger-ui.html |
| Order Service | http://localhost:8083/swagger-ui.html |
| Cart Service | http://localhost:8084/swagger-ui.html |

> To test protected endpoints in Swagger: click **Authorize**, enter `Bearer <your_token>`.

---

## 🏃 Running Services Locally (Without Docker)

If you want to run individual services locally (ensure PostgreSQL and MongoDB are running first):

```bash
# Run User Service
cd user-service
mvn spring-boot:run

# Run Product Service
cd product-service
mvn spring-boot:run

# Etc.
```

Update `application.yml` DB connection URLs to point to your local instances.

---

## 🗂️ Project Structure

```
shopwave/
├── api-gateway/               # Spring Cloud Gateway
│   └── src/main/java/com/shopwave/gateway/
│       ├── GatewayApplication.java
│       └── filter/JwtAuthenticationFilter.java
├── user-service/              # Auth + User management (PostgreSQL)
│   └── src/main/java/com/shopwave/user/
│       ├── model/             # User, Role
│       ├── dto/               # RegisterRequest, LoginRequest, AuthResponse
│       ├── repository/        # UserRepository (JPA)
│       ├── service/           # AuthService, UserService, JwtService
│       ├── filter/            # JwtAuthFilter
│       ├── config/            # SecurityConfig, OpenApiConfig
│       ├── controller/        # AuthController, UserController
│       └── exception/         # GlobalExceptionHandler
├── product-service/           # Product catalog (MongoDB)
│   └── src/main/java/com/shopwave/product/
├── order-service/             # Order lifecycle (PostgreSQL + JPA)
│   └── src/main/java/com/shopwave/order/
├── cart-service/              # Shopping cart (MongoDB)
│   └── src/main/java/com/shopwave/cart/
├── docker-compose.yml         # Full stack orchestration
├── init.sql                   # PostgreSQL schema + seed data
└── pom.xml                    # Parent Maven POM
```

---

## 🔒 Security Design

- **JWT tokens** issued on login, validated on every protected request
- **Stateless sessions** — no server-side session storage
- **Role-based access control** — `CUSTOMER` and `ADMIN` roles
- **BCrypt** password hashing
- Each service independently validates JWT using the shared secret
- API Gateway optionally forwards `X-User-Id`, `X-User-Role`, `X-User-Email` headers

---

## 🌱 Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | (base64 key) | Shared JWT signing secret — change in production! |
| `DB_HOST` | `localhost` | PostgreSQL hostname |
| `DB_NAME` | `shopwave` | PostgreSQL database name |
| `DB_USER` | `postgres` | PostgreSQL username |
| `DB_PASS` | `password` | PostgreSQL password |
| `MONGO_URI` | `mongodb://localhost:27017/...` | MongoDB connection URI |

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 👤 Author

**Your Name**
- GitHub: [@YOUR_USERNAME](https://github.com/YOUR_USERNAME)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/YOUR_PROFILE)
- Email: your@email.com

---

> ⭐ If you found this project useful, please consider giving it a star!
