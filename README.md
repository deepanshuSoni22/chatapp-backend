# Chat Application - Spring Boot Backend

A modern, real-time chat application backend built with **Spring Boot 3.5.14**, featuring JWT-based authentication, WebSocket support for live messaging, and a clean layered architecture. This project demonstrates core backend development concepts including REST APIs, security, database design, and WebSocket communication.

**Perfect for learning and portfolio building!** 🚀

---

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [WebSocket Endpoints](#websocket-endpoints)
- [Authentication](#authentication)
- [Key Components](#key-components)
- [Configuration](#configuration)

---

## Project Overview

This is a **real-time chat application backend** that allows users to:
- Create accounts and authenticate securely
- Send and receive messages in real-time
- View chat history with other users
- Receive live notifications through WebSocket connections

The backend provides both **REST APIs** for standard operations (register, login, fetch users) and **WebSocket endpoints** for real-time bidirectional messaging.

---

## Tech Stack

### Core Framework
- **Spring Boot** `3.5.14` - Application framework
- **Java** `17` - Programming language
- **Maven** - Build and dependency management

### Security & Authentication
- **Spring Security** - Authentication & authorization
- **JWT (JJWT)** `0.13.0` - JSON Web Tokens for stateless authentication
- **BCrypt** - Password encryption

### Web & Messaging
- **Spring Web** - REST API development
- **Spring WebSocket** - Real-time bidirectional communication
- **STOMP** - Messaging protocol (via SockJS)

### Database & ORM
- **Spring Data JPA** - Object-Relational Mapping
- **Hibernate** - JPA implementation
- **H2 Database** - In-memory relational database

### Utilities & Code Generation
- **Lombok** - Reduces boilerplate code
- **MapStruct** `1.6.3` - Type-safe bean mapping
- **Spring Validation** - Input validation

---

## Features

✅ **User Authentication**
- User registration with password validation
- Login with JWT token generation
- Stateless authentication using JWT

✅ **Real-Time Messaging**
- Send messages instantly via WebSocket
- Receive live notifications
- Automatic message persistence

✅ **Message History**
- Retrieve chat history between two users
- Messages stored with timestamps

✅ **User Management**
- View all available users
- Get current authenticated user info

✅ **Security**
- JWT-based stateless authentication
- Password encryption with BCrypt
- CORS support for frontend integration
- Input validation

---

## Database Schema

### Database Design
The application uses a simple but effective relational model with two main tables:

```
┌─────────────────────────────────────────────────────────────────┐
│                     DATABASE SCHEMA                             │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────┐
│              USERS TABLE                 │
├──────────────────────────────────────────┤
│ id (PK)                   Integer        │
│ username (UNIQUE)         String         │
│ password                  String         │
└──────────────────────────────────────────┘
         │
         │ 1
         ├──────────────────────────────────┐
         │                                  │
         │ N (sender)                  N (receiver)
         ↓                                  ↓
┌──────────────────────────────────────────────────────┐
│              MESSAGES TABLE                          │
├──────────────────────────────────────────────────────┤
│ id (PK)                   Integer                    │
│ sender_id (FK)            Integer → users.id         │
│ receiver_id (FK)          Integer → users.id         │
│ content                   Text                       │
│ time_stamp                LocalDateTime              │
└──────────────────────────────────────────────────────┘
```

### Table Descriptions

### USERS Table

| Column       | Type    | Constraints                 | Description               |
|:-------------|:--------|:----------------------------|:--------------------------|
| **id**       | Integer | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier    |
| **username** | String  | NOT NULL, UNIQUE            | User login username       |
| **password** | String  | NOT NULL                    | BCrypt encrypted password |

---

### MESSAGES Table

| Column          | Type          | Constraints                 | Description                          |
|:----------------|:--------------|:----------------------------|:-------------------------------------|
| **id**          | Integer       | PRIMARY KEY, AUTO_INCREMENT | Unique message identifier            |
| **sender_id**   | Integer       | NOT NULL, FOREIGN KEY       | References `users.id` (who sent)     |
| **receiver_id** | Integer       | NOT NULL, FOREIGN KEY       | References `users.id` (who receives) |
| **content**     | Text          | NOT NULL                    | Message text content                 |
| **time_stamp**  | LocalDateTime | NOT NULL                    | Message creation timestamp           |

### Entity Relationships
- **One-to-Many**: A user can have many sent messages and many received messages
- **Many-to-One**: Each message belongs to exactly one sender and one receiver
- **Cascade**: Deleting a user deletes all associated messages

---

## Project Structure

```
chat-application/
│
├── src/main/java/org/example/chatapplication/
│   │
│   ├── ChatApplication.java                 # Spring Boot entry point
│   │
│   ├── config/                              # Configuration classes
│   │   ├── SecurityConfig.java             # Spring Security setup
│   │   └── WebSocketConfig.java            # WebSocket & STOMP config
│   │
│   ├── controller/                          # REST & WebSocket endpoints
│   │   ├── AuthController.java             # Auth endpoints (register, login)
│   │   ├── MessageController.java          # Message history endpoints
│   │   └── MessageWebSocketController.java # WebSocket messaging
│   │
│   ├── service/                             # Business logic layer
│   │   ├── UserService.java                # User operations
│   │   ├── MessageService.java             # Message operations
│   │   ├── JwtService.java                 # JWT token generation/validation
│   │   └── CustomUserDetailsService.java   # User details loading
│   │
│   ├── repository/                          # Database access (DAO pattern)
│   │   ├── UserRepository.java             # User CRUD operations
│   │   └── MessageRepository.java          # Message CRUD operations
│   │
│   ├── entity/                              # JPA entity classes
│   │   ├── User.java                       # User entity
│   │   └── Message.java                    # Message entity
│   │
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── request/                        # API request DTOs
│   │   │   └── UserRequest.java
│   │   │   └── MessageRequest.java
│   │   └── response/                       # API response DTOs
│   │       ├── UserResponse.java
│   │       ├── MessageResponse.java
│   │       └── LoginResponse.java
│   │
│   ├── mapper/                              # MapStruct mappers
│   │   ├── UserMapper.java                 # Entity ↔ DTO conversion
│   │   └── MessageMapper.java
│   │
│   ├── exception/                           # Exception handling
│   │   ├── GlobalExceptionHandler.java     # Centralized error handling
│   │   └── custom/                         # Custom exceptions
│   │       └── UsernameAlreadyExistsException.java
│   │
│   ├── filter/                              # Security filters
│   │   └── JwtAuthenticationFilter.java    # JWT validation filter
│   │
│   └── interceptor/                         # WebSocket interceptors
│       └── JwtChannelInterceptor.java      # JWT validation for WebSocket
│
├── src/main/resources/
│   ├── application.properties               # Main configuration
│   ├── application-dev.properties          # Development profile
│   └── application-prod.properties         # Production profile
│
├── src/test/java/                          # Unit & Integration tests
│   └── org/example/chatapplication/
│       └── ChatApplicationTests.java
│
├── pom.xml                                  # Maven dependencies
└── mvnw / mvnw.cmd                         # Maven wrapper
```

### Architectural Layers

```
┌─────────────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                             │
│  Controllers (REST & WebSocket endpoints)                   │
│  Handles HTTP requests and WebSocket connections           │
└────────────────────────┬────────────────────────────────────┘
                         │
┌─────────────────────────▼────────────────────────────────────┐
│              BUSINESS LOGIC LAYER                            │
│  Services (UserService, MessageService, JwtService)         │
│  Contains core application logic & validations              │
└────────────────────────┬────────────────────────────────────┘
                         │
┌─────────────────────────▼────────────────────────────────────┐
│              DATA ACCESS LAYER                               │
│  Repositories (UserRepository, MessageRepository)           │
│  Handles database operations via Spring Data JPA            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌─────────────────────────▼────────────────────────────────────┐
│              DATABASE LAYER                                  │
│  H2 Database (in-memory for development)                    │
│  Persists data with JPA/Hibernate ORM                       │
└─────────────────────────────────────────────────────────────┘
```

### Cross-Cutting Concerns

```
DTO Conversion     → MapStruct (Entity ↔ DTO mapping)
Security           → SecurityConfig + JwtAuthenticationFilter
Authentication     → JwtService + CustomUserDetailsService
Validation         → Spring Validation (@Valid, @NotNull, etc.)
Exception Handling → GlobalExceptionHandler
WebSocket Security → JwtChannelInterceptor
```

---

## API Endpoints

### Authentication Endpoints

#### 1. Register a New User
```
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "john_doe"
}
```

**Errors:**
- `400 Bad Request` - Invalid input
- `409 Conflict` - Username already exists

---

#### 2. Login (Get JWT Token)
```
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Errors:**
- `401 Unauthorized` - Invalid credentials

---

#### 3. Get All Users (Except Current User)
```
GET /api/v1/auth/users
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "username": "alice"
  },
  {
    "id": 3,
    "username": "bob"
  }
]
```

**Errors:**
- `401 Unauthorized` - Missing or invalid token

---

#### 4. Get Current User Info
```
GET /api/v1/auth/me
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK):**
```
john_doe
```

---

### Message Endpoints

#### 1. Get Chat History with Another User
```
GET /api/v1/message/{otherUserId}
Authorization: Bearer <JWT_TOKEN>
```

**Path Parameters:**
- `otherUserId` - ID of the other user in the conversation

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "senderUsername": "john_doe",
    "receiverUsername": "alice",
    "content": "Hello Alice!",
    "timeStamp": "2024-06-08T10:30:00"
  },
  {
    "id": 2,
    "senderUsername": "alice",
    "receiverUsername": "john_doe",
    "content": "Hi John!",
    "timeStamp": "2024-06-08T10:31:00"
  }
]
```

**Errors:**
- `401 Unauthorized` - Missing or invalid token
- `404 Not Found` - User not found

---

## WebSocket Endpoints

### Connection Endpoint

**WebSocket URL:**
```
ws://localhost:8080/ws
```

**Query Parameter:**
```
ws://localhost:8080/ws?token=<JWT_TOKEN>
```

The JWT token can be passed as:
1. Query parameter: `?token=<JWT_TOKEN>`
2. HTTP header: `Authorization: Bearer <JWT_TOKEN>` (during handshake)

### Message Format

#### Subscribe to Receive Messages
```
SUBSCRIBE
destination: /user/queue/messages
id: 1
```

#### Send a Message
```
SEND
destination: /app/chat.send
content-length: 100

{
  "receiverId": 2,
  "content": "Hello Bob!"
}
```

#### Receive Message (Server Sends)
```json
{
  "id": 3,
  "senderUsername": "john_doe",
  "senderUserId": 1,
  "receiverUsername": "bob",
  "receiverId": 2,
  "content": "Hello Bob!",
  "timeStamp": "2024-06-08T10:35:00"
}
```

### WebSocket Message Flow

```
Client A                                           Client B
   │                                                 │
   │────── Connect to /ws ───────────────────────────│
   │                                                 │
   │─ Subscribe to /user/queue/messages ─────────────│
   │                                                 │
   │─ Send to /app/chat.send ─────────────────────────│
   │      {receiverId: 2, content: "Hi!"}            │
   │                                                 │
   │ ← Broadcast message (saved to DB) ─────────────→│
   │                                                 │
   │ ← Message via /user/queue/messages ─────────────│
   │      (confirmation)                             │
   │                  Message via /user/queue/ ←─────│
   │                  messages (received)             │
```

---

## Authentication

### JWT (JSON Web Token) Overview

This application uses **JWT** for stateless authentication:

1. **Registration**: User registers with username & password
2. **Login**: User provides credentials, receives a JWT token
3. **API Calls**: Client includes token in `Authorization` header
4. **Validation**: Server validates token signature and expiration
5. **Access**: If token is valid, request is processed

### How to Use JWT Token

#### Step 1: Get Token (Login)
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTcxNzg1MDYwMH0.signature"
}
```

#### Step 2: Use Token in API Requests
```bash
curl -X GET http://localhost:8080/api/v1/auth/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Token Structure

A JWT token consists of 3 parts: `HEADER.PAYLOAD.SIGNATURE`

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTcxNzg1MDYwMH0.
signature_here

HEADER:   {"alg": "HS256", "typ": "JWT"}
PAYLOAD:  {"sub": "john_doe", "iat": 1717850600}
SIGNATURE: HMACSHA256(header.payload, secret)
```

### Security Configuration

- **Algorithm**: HMAC SHA-256
- **Secret Key**: Environment variable `JWT_SECRET`
- **Session Management**: Stateless (no session storage)
- **Password Encryption**: BCrypt with salt
- **CORS**: Enabled for development

---

## Key Components

### Controllers

**AuthController** - Handles authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `GET /api/v1/auth/users` - List all users
- `GET /api/v1/auth/me` - Current user info

**MessageController** - Handles message REST API
- `GET /api/v1/message/{otherUserId}` - Chat history

**MessageWebSocketController** - Handles WebSocket messaging
- `@MessageMapping("/chat.send")` - Receives and broadcasts messages

### Services

**UserService** - User business logic
- `register()` - Create new user with validation
- `getAllUsersExcept()` - Fetch all users except current

**MessageService** - Message business logic
- `saveMessage()` - Persist message to database
- `getMessageHistory()` - Fetch conversation between users

**JwtService** - JWT token management
- `generateToken()` - Create JWT token
- `extractUsername()` - Get username from token
- `isTokenValid()` - Validate token signature & expiration

**CustomUserDetailsService** - Spring Security integration
- `loadUserByUsername()` - Load user for authentication

### Filters & Interceptors

**JwtAuthenticationFilter** - Validates JWT in all requests
- Extracts token from `Authorization` header
- Validates token signature and expiration
- Sets authentication in security context

**JwtChannelInterceptor** - Validates JWT for WebSocket connections
- Extracts token from STOMP headers
- Authenticates before allowing subscription

---

## Learning Outcomes

By studying this project, you'll learn:

✅ **Spring Boot Architecture**
- Layered architecture (Controller → Service → Repository)
- Dependency injection with Spring
- Spring Boot auto-configuration

✅ **Security Implementation**
- JWT token generation and validation
- Spring Security configuration
- Password encryption with BCrypt
- Authentication & Authorization

✅ **Web Development**
- RESTful API design principles
- HTTP methods and status codes
- Request/Response DTOs
- Input validation

✅ **Real-Time Communication**
- WebSocket protocol basics
- STOMP messaging
- Server-to-client push notifications

✅ **Database Design**
- Relational schema design
- One-to-Many relationships
- Foreign keys and cascading
- JPA/Hibernate ORM mapping

✅ **Best Practices**
- Separation of concerns
- Mapper pattern with MapStruct
- Exception handling
- Configuration management
- Clean code principles

---

## Project Information

- **Version**: 0.0.1-SNAPSHOT
- **Java Version**: 17
- **Spring Boot**: 3.5.14
- **License**: Open Source
- **Purpose**: Portfolio & Learning Project
