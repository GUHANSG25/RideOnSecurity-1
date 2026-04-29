# RideOn — Bus Ticket Booking Platform (Backend)

> A Spring Boot 4 REST API providing authentication, ride booking, bus/route/trip management, operator lifecycle, and Razorpay payment integration for the RideOn platform.

**Repository:** [RideOnSecurity-1](https://github.com/GUHANSG25/RideOnSecurity-1)  
**Frontend:** [ride_on_frontend](https://github.com/GUHANSG25/ride_on_frontend)

---

## Table of Contents

1. [System Overview](#system-overview)
2. [Tech Stack](#tech-stack)
3. [Prerequisites](#prerequisites)
4. [Directory Structure](#directory-structure)
5. [Feature-Based Architecture](#feature-based-architecture)
6. [Feature Flow Explanations](#feature-flow-explanations)
7. [Getting Started](#getting-started)
8. [Environment Configuration](#environment-configuration)
9. [API Overview](#api-overview)
10. [Security Model](#security-model)

---

## System Overview

`RideOnSecurity-1` is the backend service for the RideOn bus ticket booking platform. It exposes a stateless REST API consumed by the React frontend. All business logic, data persistence, security enforcement, OTP delivery (Twilio), and payment order creation (Razorpay) are handled here.

| Concern | Handled By |
|---|---|
| Authentication & JWT issuance | Spring Security + custom JWT filter |
| OTP generation & SMS delivery | Twilio SDK |
| Payment order creation | Razorpay SDK |
| Data persistence | Spring Data JPA + MySQL |
| Role-based access control | Spring Security method/URL security |
| Input validation | Spring Boot Validation (Jakarta Bean Validation) |

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Framework | Spring Boot | 4.0.5 |
| Web | Spring Web MVC | (managed by Boot) |
| Security | Spring Security | (managed by Boot) |
| ORM | Spring Data JPA + Hibernate Core | (managed by Boot) |
| Database | MySQL | 8+ |
| Validation | Spring Boot Starter Validation | (managed by Boot) |
| OTP / SMS | Twilio Java SDK | 10.1.0 |
| Payments | Razorpay Java SDK | 1.4.3 |
| Build Tool | Maven (wrapper included) | 3.9+ |

---

## Prerequisites

- **Java 21** (`java -version`)
- **Maven 3.9+** — or use the included `mvnw` / `mvnw.cmd` wrapper (no separate install needed)
- **MySQL 8+** — database created and accessible
- **Twilio account** — Account SID, Auth Token, verified phone number
- **Razorpay account** — Key ID and Key Secret
- **Git**

---

## Directory Structure

```
RideOnSecurity-1/
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties          # Pins Maven version
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sample/RideOnSecurity_1/
│   │   │       ├── RideOnSecurity1Application.java   # @SpringBootApplication entry point
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── SecurityConfig.java            # Filter chain, CORS, permit rules
│   │   │       │   ├── ApplicationConfig.java         # Beans: PasswordEncoder, AuthenticationManager
│   │   │       │   ├── TwilioConfig.java              # Twilio client bean from properties
│   │   │       │   └── RazorpayConfig.java            # Razorpay client bean from properties
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java            # POST /rideon/login, /rideon/register, /rideon/verify-otp
│   │   │       │   ├── UserController.java            # GET/PUT /rideon/profile
│   │   │       │   ├── BusController.java             # CRUD /rideon/buses
│   │   │       │   ├── RouteController.java           # CRUD /rideon/routes
│   │   │       │   ├── TripController.java            # CRUD /rideon/trips
│   │   │       │   ├── BookingController.java         # POST/GET/DELETE /rideon/bookings
│   │   │       │   ├── OperatorController.java        # GET/PUT /rideon/operators (admin)
│   │   │       │   └── PaymentController.java         # POST /rideon/payments/create-order, /verify
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── AuthService.java               # Registration, login, token generation
│   │   │       │   ├── JwtService.java                # Token generation, validation, claim extraction
│   │   │       │   ├── OtpService.java                # OTP generation, Twilio SMS dispatch, validation
│   │   │       │   ├── UserService.java               # Profile read/update
│   │   │       │   ├── BusService.java                # Bus CRUD business logic
│   │   │       │   ├── RouteService.java              # Route CRUD business logic
│   │   │       │   ├── TripService.java               # Trip scheduling, search by route+date
│   │   │       │   ├── BookingService.java            # Booking creation, retrieval, cancellation
│   │   │       │   ├── OperatorService.java           # Operator listing, approval, rejection
│   │   │       │   └── PaymentService.java            # Razorpay order creation, signature verification
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java            # JPA repo for User
│   │   │       │   ├── BusRepository.java             # JPA repo for Bus
│   │   │       │   ├── RouteRepository.java           # JPA repo for Route
│   │   │       │   ├── TripRepository.java            # JPA repo for Trip (query by route + date)
│   │   │       │   ├── BookingRepository.java         # JPA repo for Booking (query by userId)
│   │   │       │   └── OtpRepository.java             # JPA repo for OtpRecord (TTL-based lookup)
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── User.java                      # @Entity: id, name, email, mobile, password, role
│   │   │       │   ├── Bus.java                       # @Entity: id, name, capacity, type, operator
│   │   │       │   ├── Route.java                     # @Entity: id, origin, destination, distance
│   │   │       │   ├── Trip.java                      # @Entity: id, bus, route, departureTime, fare, status
│   │   │       │   ├── Booking.java                   # @Entity: id, trip, user, seats, status, createdAt
│   │   │       │   ├── OtpRecord.java                 # @Entity: id, mobile, otp, expiresAt
│   │   │       │   └── enums/
│   │   │       │       ├── Role.java                  # ROLE_ADMIN, ROLE_OPERATOR, ROLE_CUSTOMER
│   │   │       │       ├── TripStatus.java            # SCHEDULED, CANCELLED, COMPLETED
│   │   │       │       └── BookingStatus.java         # CONFIRMED, CANCELLED
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── LoginRequest.java              # email, password
│   │   │       │   ├── RegisterRequest.java           # name, email, mobile, password, role
│   │   │       │   ├── OtpRequest.java                # mobile
│   │   │       │   ├── OtpVerifyRequest.java          # mobile, otp
│   │   │       │   ├── AuthResponse.java              # token, email, role
│   │   │       │   ├── BusRequest.java                # name, capacity, type
│   │   │       │   ├── RouteRequest.java              # origin, destination, distance
│   │   │       │   ├── TripRequest.java               # busId, routeId, departureTime, fare
│   │   │       │   ├── BookingRequest.java            # tripId, seats
│   │   │       │   ├── ProfileUpdateRequest.java      # name, email, mobile
│   │   │       │   └── PaymentOrderRequest.java       # amount, currency, bookingId
│   │   │       │
│   │   │       ├── filter/
│   │   │       │   └── JwtAuthenticationFilter.java   # OncePerRequestFilter: extract → validate → set context
│   │   │       │
│   │   │       └── exception/
│   │   │           ├── GlobalExceptionHandler.java    # @RestControllerAdvice: maps exceptions → HTTP responses
│   │   │           ├── ResourceNotFoundException.java # 404 wrapper
│   │   │           └── UnauthorizedException.java     # 403 wrapper
│   │   │
│   │   └── resources/
│   │       └── application.properties                 # DB, JWT, Twilio, Razorpay, CORS config
│   │
│   └── test/
│       └── java/
│           └── com/sample/RideOnSecurity_1/
│               └── RideOnSecurity1ApplicationTests.java
│
├── pom.xml                                            # Maven dependencies & build config
├── mvnw                                               # Unix Maven wrapper script
└── mvnw.cmd                                           # Windows Maven wrapper script
```

---

## Feature-Based Architecture

The backend is organized into eight vertical feature domains. Each domain has a controller (HTTP boundary), service (business logic), repository (data access), model (JPA entity), and DTOs (request/response shapes).

| Feature | Controller | Service | Key Entities | External Integration |
|---|---|---|---|---|
| Auth | `AuthController` | `AuthService`, `JwtService` | `User` | — |
| OTP | `AuthController` | `OtpService` | `OtpRecord` | **Twilio** |
| User | Profile | `UserController` | `UserService` | `User` | — |
| Bus | `BusController` | `BusService` | `Bus` | — |
| Route | `RouteController` | `RouteService` | `Route` | — |
| Trip | `TripController` | `TripService` | `Trip` | — |
| Booking | `BookingController` | `BookingService` | `Booking` | — |
| Operator | `OperatorController` | `OperatorService` | `User` (ROLE_OPERATOR) | — |
| Payment | `PaymentController` | `PaymentService` | `Booking` | **Razorpay** |

Cross-cutting concerns (JWT validation, exception mapping, CORS) are handled globally via `JwtAuthenticationFilter`, `GlobalExceptionHandler`, and `SecurityConfig` — keeping controllers and services free of infrastructure boilerplate.

---

## Feature Flow Explanations

### 1. Registration Flow

```
POST /rideon/register { name, email, mobile, password, role }
  → AuthController.register()
    → AuthService.register()
      → Validates input (@Valid on RegisterRequest)
      → BCryptPasswordEncoder.encode(password)
      → UserRepository.save(user)
      → JwtService.generateToken(user)
    ← AuthResponse { token, email, role }
← 201 Created
```

---

### 2. Login Flow

```
POST /rideon/login { email, password }
  → AuthController.login()
    → AuthService.authenticate()
      → AuthenticationManager.authenticate(UsernamePasswordAuthToken)
      → UserRepository.findByEmail(email)
      → JwtService.generateToken(user)
    ← AuthResponse { token, email, role }
← 200 OK
```

---

### 3. OTP Flow (Twilio)

```
POST /rideon/send-otp { mobile }
  → AuthController.sendOtp()
    → OtpService.generateAndSend(mobile)
      → Generates 6-digit OTP
      → OtpRecord saved with expiresAt = now + 5 min
      → Twilio MessageCreator.create(mobile, fromNumber, otp)
← 200 OK

POST /rideon/verify-otp { mobile, otp }
  → AuthController.verifyOtp()
    → OtpService.verify(mobile, otp)
      → OtpRepository.findByMobile(mobile)
      → Checks otp match + not expired
      → Marks OtpRecord as used
    ← AuthResponse { token, email, role }
← 200 OK
```

---

### 4. JWT Filter Chain (every protected request)

```
Inbound request → JwtAuthenticationFilter.doFilterInternal()
  → Reads Authorization header: "Bearer <token>"
  → JwtService.extractUsername(token)
  → UserDetailsService.loadUserByUsername(email)
  → JwtService.isTokenValid(token, userDetails)
    → Validates signature + expiry
  → SecurityContextHolder.setAuthentication(token)
→ Request proceeds to Controller
```

---

### 5. Bus Search & Trip Query Flow

```
GET /rideon/trips?from=X&to=Y&date=Z
  → TripController.searchTrips()
    → TripService.findAvailable(from, to, date)
      → RouteRepository.findByOriginAndDestination(from, to)
      → TripRepository.findByRouteAndDepartureDateAndStatus(route, date, SCHEDULED)
    ← List<Trip> with Bus and Route details embedded
← 200 OK
```

---

### 6. Booking Creation Flow

```
POST /rideon/bookings { tripId, seats }   [ROLE_CUSTOMER]
  → BookingController.createBooking()
    → BookingService.create(bookingRequest, authenticatedUser)
      → TripRepository.findById(tripId) — validates trip exists + SCHEDULED
      → Checks seat availability (trip.capacity - existingBookings)
      → Booking entity created with status = CONFIRMED
      → BookingRepository.save(booking)
    ← Booking DTO { bookingId, trip, seats, status, createdAt }
← 201 Created
```

---

### 7. Payment Flow (Razorpay)

```
POST /rideon/payments/create-order { amount, currency, bookingId }   [ROLE_CUSTOMER]
  → PaymentController.createOrder()
    → PaymentService.createRazorpayOrder(amount, currency)
      → RazorpayClient.orders.create({ amount, currency, receipt })
    ← { orderId, amount, currency, key }
← 200 OK   (frontend opens Razorpay checkout UI)

POST /rideon/payments/verify { razorpayOrderId, razorpayPaymentId, razorpaySignature, bookingId }
  → PaymentController.verifyPayment()
    → PaymentService.verifySignature(orderId, paymentId, signature)
      → HMAC-SHA256(orderId + "|" + paymentId, razorpaySecret)
      → Compares with razorpaySignature
    → BookingRepository.updatePaymentStatus(bookingId, PAID)
    ← { success: true, bookingId }
← 200 OK
```

---

### 8. Operator Approval Flow

```
GET /rideon/operators?status=PENDING   [ROLE_ADMIN]
  → OperatorController.getPendingOperators()
    → OperatorService.findByStatus(PENDING)
    ← List<User> where role = ROLE_OPERATOR, status = PENDING
← 200 OK

PUT /rideon/operators/{id}/approve     [ROLE_ADMIN]
  → OperatorController.approveOperator()
    → OperatorService.approve(id)
      → UserRepository.findById(id)
      → Sets operator status = APPROVED
      → UserRepository.save(operator)
    ← Updated operator DTO
← 200 OK
```

---

### 9. Global Exception Handling

```
Any service throws ResourceNotFoundException
  → GlobalExceptionHandler.handleNotFound()
    ← { status: 404, message: "...", timestamp }

Any @Valid constraint fails
  → GlobalExceptionHandler.handleValidationErrors()
    ← { status: 400, errors: { field: "message" } }

Unauthenticated request reaches protected endpoint
  → Spring Security 401 response (before hitting controller)

Authenticated user accesses wrong-role endpoint
  → Spring Security 403 response (before hitting controller)
```

---

## Getting Started

### 1. Clone

```bash
git clone https://github.com/GUHANSG25/RideOnSecurity-1.git
cd RideOnSecurity-1
```

### 2. Create the Database

```sql
CREATE DATABASE rideon_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Properties

Edit `src/main/resources/application.properties` (see [Environment Configuration](#environment-configuration)).

### 4. Run

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Server starts at `http://localhost:8080` by default.

### 5. Build JAR

```bash
./mvnw clean package -DskipTests
java -jar target/RideOnSecurity-1-0.0.1-SNAPSHOT.jar
```

---

## Environment Configuration

All configuration lives in `src/main/resources/application.properties`:

```properties
# ── Server ──────────────────────────────────────────────
server.port=8080

# ── Database ────────────────────────────────────────────
spring.datasource.url=jdbc:mysql://localhost:3306/rideon_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# ── JWT ─────────────────────────────────────────────────
jwt.secret=your-256-bit-base64-encoded-secret
jwt.expiration=86400000        # 24 hours in milliseconds

# ── CORS ────────────────────────────────────────────────
cors.allowed-origins=http://localhost:5173

# ── Twilio ──────────────────────────────────────────────
twilio.account-sid=ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
twilio.auth-token=your_twilio_auth_token
twilio.from-number=+1XXXXXXXXXX

# ── Razorpay ────────────────────────────────────────────
razorpay.key-id=rzp_test_XXXXXXXXXX
razorpay.key-secret=your_razorpay_key_secret
```

> **Never commit real secrets.** Use environment variable substitution (`${ENV_VAR}`) or Spring profiles for production deployments.

---

## API Overview

### Public Endpoints (no token required)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/rideon/register` | Register a new user |
| `POST` | `/rideon/login` | Authenticate, receive JWT |
| `POST` | `/rideon/send-otp` | Send OTP to mobile via Twilio |
| `POST` | `/rideon/verify-otp` | Verify OTP, receive JWT |

### Customer Endpoints (`ROLE_CUSTOMER`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/rideon/trips` | Search trips by `from`, `to`, `date` |
| `POST` | `/rideon/bookings` | Create a booking |
| `GET` | `/rideon/bookings` | Get own bookings |
| `DELETE` | `/rideon/bookings/{id}` | Cancel a booking |
| `POST` | `/rideon/payments/create-order` | Create Razorpay order |
| `POST` | `/rideon/payments/verify` | Verify payment signature |

### Operator Endpoints (`ROLE_OPERATOR`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/rideon/buses` | List own buses |
| `POST` | `/rideon/buses` | Add a bus |
| `PUT` | `/rideon/buses/{id}` | Update a bus |
| `DELETE` | `/rideon/buses/{id}` | Delete a bus |
| `GET` | `/rideon/routes` | List own routes |
| `POST` | `/rideon/routes` | Add a route |
| `DELETE` | `/rideon/routes/{id}` | Delete a route |
| `GET` | `/rideon/trips` | List own trips |
| `POST` | `/rideon/trips` | Schedule a trip |
| `PUT` | `/rideon/trips/{id}` | Update a trip |

### Admin Endpoints (`ROLE_ADMIN`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/rideon/operators` | List all operators |
| `GET` | `/rideon/operators?status=PENDING` | List pending operators |
| `PUT` | `/rideon/operators/{id}/approve` | Approve an operator |
| `PUT` | `/rideon/operators/{id}/reject` | Reject an operator |
| `POST` | `/rideon/operators` | Register a new operator |

### All Authenticated Roles

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/rideon/profile` | Get own profile |
| `PUT` | `/rideon/profile` | Update own profile |

All protected endpoints require: `Authorization: Bearer <token>`

---

## Security Model

### Stateless JWT

The backend holds no session state. Every request is independently authenticated via the `JwtAuthenticationFilter` which runs before any controller is reached.

### Filter Chain Order

```
Request
  → CORS filter
  → JwtAuthenticationFilter
      → extract token from Authorization header
      → validate signature & expiry via JwtService
      → load UserDetails, populate SecurityContext
  → Spring Security authorization check (role/URL rules)
  → DispatcherServlet → Controller
```

### Password Storage

All passwords are hashed with **BCrypt** via Spring Security's `PasswordEncoder`. Plaintext passwords are never stored or logged.

### Role Hierarchy

| Role | Can Access |
|---|---|
| `ROLE_CUSTOMER` | Own bookings, trip search, payments, profile |
| `ROLE_OPERATOR` | Own buses, routes, trips, profile |
| `ROLE_ADMIN` | Everything above + operator management, platform-wide data |

### Razorpay Signature Verification

Payment completion is verified server-side by recomputing `HMAC-SHA256(orderId + "|" + paymentId, keySecret)` and comparing it against the signature sent by the frontend. This prevents booking confirmation without a genuine Razorpay payment.

### CORS

The `SecurityConfig` restricts cross-origin requests to the configured `cors.allowed-origins` value. Wildcard `*` is not used in production configuration.