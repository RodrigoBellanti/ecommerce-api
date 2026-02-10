# E-commerce REST API

A comprehensive e-commerce backend built with Spring Boot 4, featuring advanced inventory management, order processing with automatic stock control, and complete CRUD operations for products and categories. This project demonstrates enterprise-level Java development patterns and best practices.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)

## 🚀 Features

### Core Functionality
- **Product Management**: Full CRUD with category assignment, stock tracking, and price management
- **Category Management**: Hierarchical organization with cascade operations
- **Order Processing**: Complete order lifecycle from creation to delivery
- **Inventory Control**: Automatic stock deduction on order creation and restoration on cancellation
- **Order Status Workflow**: State machine validation (PENDING → PROCESSING → SHIPPED → DELIVERED)

### Technical Features
- **Advanced JPA Relationships**: OneToMany, ManyToOne with proper cascading and orphan removal
- **MapStruct Integration**: Automatic DTO ↔ Entity mapping with zero boilerplate
- **Comprehensive Validation**: Bean Validation (JSR-380) on all input DTOs
- **Global Exception Handling**: Custom exceptions with detailed error responses
- **Pagination & Filtering**: All list endpoints support pagination, sorting, and filtering
- **Interactive API Documentation**: Swagger/OpenAPI 3.0 with tryout functionality
- **Transactional Integrity**: Proper @Transactional annotations with rollback support
- **Docker Compose**: One-command PostgreSQL setup

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 4.0.2**
    - Spring Data JPA
    - Spring Web
    - Spring Validation
- **PostgreSQL 16** (via Docker)
- **MapStruct 1.5.5** (compile-time DTO mapping)
- **Springdoc OpenAPI 2.7.0** (Swagger UI)
- **Lombok** (reduces boilerplate)
- **Maven** (dependency management)
- **Docker & Docker Compose**

## 📋 Prerequisites

- **JDK 17** or higher
- **Maven 3.6+**
- **Docker Desktop** (for PostgreSQL)
- **Git**

## ⚙️ Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/RodrigoBellanti/ecommerce-api.git
cd ecommerce-api
```

### 2. Start PostgreSQL with Docker
```bash
docker-compose up -d
```

This will start PostgreSQL on port 5432 with:
- Database: `ecommerce`
- Username: `admin`
- Password: `admin123`

### 3. Build the project
```bash
mvn clean install
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## 📖 API Documentation

### Swagger UI (Interactive)

Access the complete interactive API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

Here you can:
- Browse all available endpoints
- Test API calls directly from the browser
- View request/response schemas
- See validation rules

### OpenAPI Specification

Raw OpenAPI spec available at:
```
http://localhost:8080/v3/api-docs
```

## 🔌 Main API Endpoints

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create new category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category (cascades to products) |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List products (paginated, filterable) |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/low-stock` | Get products below stock threshold |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

**Query Parameters for GET /api/products:**
- `categoryId` - Filter by category
- `minPrice` & `maxPrice` - Price range filter
- `page` - Page number (0-indexed)
- `size` - Items per page
- `sortBy` - Field to sort by (default: "id")
- `direction` - Sort direction (ASC/DESC)

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | List orders (paginated, filterable) |
| GET | `/api/orders/{id}` | Get order by ID with items |
| POST | `/api/orders` | Create new order (validates stock) |
| PATCH | `/api/orders/{id}/status` | Update order status |
| POST | `/api/orders/{id}/cancel` | Cancel order (restores stock) |

**Query Parameters for GET /api/orders:**
- `status` - Filter by order status (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `customerEmail` - Filter by customer email
- `page`, `size`, `sortBy`, `direction` - Pagination & sorting

## 📊 Database Schema

```
┌──────────────┐
│  categories  │
│──────────────│
│ id (PK)      │
│ name         │
│ description  │
│ created_at   │
│ updated_at   │
└──────┬───────┘
       │ 1:N
┌──────▼───────┐
│   products   │
│──────────────│
│ id (PK)      │
│ name         │
│ description  │
│ price        │
│ stock        │
│ category_id  │◄─────┐
│ created_at   │      │
│ updated_at   │      │ N:1
└──────┬───────┘      │
       │ 1:N          │
┌──────▼───────┐      │
│ order_items  │      │
│──────────────│      │
│ id (PK)      │      │
│ order_id     │      │
│ product_id   ├──────┘
│ quantity     │
│ unit_price   │
│ subtotal     │
└──────┬───────┘
       │ N:1
┌──────▼───────┐
│   orders     │
│──────────────│
│ id (PK)      │
│ customer_name│
│ customer_email│
│ shipping_addr│
│ status       │
│ total_amount │
│ created_at   │
│ updated_at   │
└──────────────┘
```

## 🎯 Architecture & Design Patterns

### Layered Architecture
```
┌─────────────────┐
│   Controllers   │  REST endpoints, request validation
├─────────────────┤
│    Services     │  Business logic, transactions
├─────────────────┤
│  Repositories   │  Data access (Spring Data JPA)
├─────────────────┤
│   Entities      │  JPA entities with relationships
└─────────────────┘
```

### Key Patterns Used

**Repository Pattern**
- Spring Data JPA repositories
- Custom query methods
- Pagination support

**DTO Pattern**
- Separate request/response DTOs
- MapStruct for automatic mapping
- Prevents exposure of internal entities

**Service Layer Pattern**
- Business logic encapsulation
- Transaction management
- Exception handling

**Exception Handling Strategy**
- Custom exceptions (ResourceNotFoundException, InsufficientStockException, etc.)
- Global exception handler (@RestControllerAdvice)
- Consistent error response format

## 📝 Example Usage

### 1. Create a Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and accessories"
  }'
```

### 2. Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell XPS 15",
    "description": "High performance laptop",
    "price": 1500.00,
    "stock": 10,
    "categoryId": 1
  }'
```

### 3. Create an Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Juan Pérez",
    "customerEmail": "juan@example.com",
    "shippingAddress": "Av. Libertador 1234, CABA, Argentina",
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

**What happens:**
- Stock is automatically reduced (10 → 8)
- Total amount is calculated (1500 × 2 = 3000)
- Order status is set to PENDING
- All changes are atomic (transactional)

### 4. Get Products with Filters
```bash
# Get products by category
curl "http://localhost:8080/api/products?categoryId=1&page=0&size=10"

# Get products by price range
curl "http://localhost:8080/api/products?minPrice=100&maxPrice=2000"

# Get low stock products
curl "http://localhost:8080/api/products/low-stock?threshold=5"
```

### 5. Cancel an Order (Restores Stock)
```bash
curl -X POST http://localhost:8080/api/orders/1/cancel
```

Stock is automatically restored (8 → 10).

## ✅ Input Validation

All endpoints validate input data with detailed error messages:

### Product Validation Rules
- `name`: Required, 2-200 characters
- `description`: Optional, max 1000 characters
- `price`: Required, must be > 0
- `stock`: Required, cannot be negative
- `categoryId`: Required, must reference existing category

### Order Validation Rules
- `customerName`: Required, 2-100 characters
- `customerEmail`: Required, valid email format
- `shippingAddress`: Required, 10-200 characters
- `items`: Required, at least one item
- `quantity`: Required, minimum 1

**Validation Error Response:**
```json
{
  "timestamp": "2026-02-09T21:45:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "path": "/api/products",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than 0"
  }
}
```

## 🔒 Business Rules

### Stock Management
- Stock is validated before order creation
- Insufficient stock throws `InsufficientStockException`
- Stock is automatically deducted on order confirmation
- Stock is restored on order cancellation

### Order Status Transitions
Valid transitions:
- `PENDING` → `PROCESSING` or `CANCELLED`
- `PROCESSING` → `SHIPPED` or `CANCELLED`
- `SHIPPED` → `DELIVERED`
- `DELIVERED` → (final state, cannot change)
- `CANCELLED` → (final state, cannot change)

Invalid transitions throw `InvalidStatusTransitionException`.

## 🐳 Docker Configuration

### docker-compose.yml
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: ecommerce-postgres
    environment:
      POSTGRES_DB: ecommerce
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

### Useful Docker Commands
```bash
# Start PostgreSQL
docker-compose up -d

# View logs
docker-compose logs -f

# Stop PostgreSQL
docker-compose down

# Stop and remove data
docker-compose down -v

# Access PostgreSQL shell
docker exec -it ecommerce-postgres psql -U admin -d ecommerce
```

## 🏗️ Project Structure

```
src/main/java/com/ecommerce/ecommerce_api/
├── config/              # Configuration classes
│   └── OpenApiConfig.java
├── controller/          # REST endpoints
│   ├── CategoryController.java
│   ├── ProductController.java
│   └── OrderController.java
├── dto/                 # Data Transfer Objects
│   ├── CategoryDTO.java
│   ├── ProductDTO.java
│   ├── OrderDTO.java
│   ├── OrderItemDTO.java
│   └── CreateOrderRequest.java
├── entity/              # JPA Entities
│   ├── Category.java
│   ├── Product.java
│   ├── Order.java
│   └── OrderItem.java
├── exception/           # Custom exceptions & handlers
│   ├── ResourceNotFoundException.java
│   ├── InsufficientStockException.java
│   ├── DuplicateResourceException.java
│   ├── InvalidStatusTransitionException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── mapper/              # MapStruct mappers
│   ├── CategoryMapper.java
│   ├── ProductMapper.java
│   ├── OrderMapper.java
│   └── OrderItemMapper.java
├── repository/          # Spring Data repositories
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
├── service/             # Business logic layer
│   ├── CategoryService.java
│   ├── ProductService.java
│   └── OrderService.java
└── EcommerceApiApplication.java
```

## 🔜 Future Enhancements

- [ ] Unit & Integration Tests (JUnit 5 + Mockito)
- [ ] CI/CD Pipeline with GitHub Actions
- [ ] Spring Security with JWT Authentication
- [ ] User Management with Roles (ADMIN, CUSTOMER)
- [ ] Shopping Cart functionality
- [ ] Payment Integration
- [ ] Product Reviews & Ratings
- [ ] Image Upload for Products
- [ ] Email Notifications (order confirmation, shipping updates)
- [ ] Sales Reports & Analytics
- [ ] Inventory Alerts (low stock notifications)
- [ ] Product Search with Elasticsearch
- [ ] Caching with Redis
- [ ] Rate Limiting
- [ ] API Versioning
- [ ] Deployment to Cloud (AWS/Heroku/Railway)

## 🧪 Testing

### Manual Testing with Swagger

1. Start the application
2. Open http://localhost:8080/swagger-ui/index.html
3. Test each endpoint with the "Try it out" button

### Testing Business Logic

**Test Stock Control:**
1. Create product with stock=5
2. Create order with quantity=3 → stock becomes 2
3. Cancel order → stock restored to 5

**Test Status Transitions:**
1. Create order (status: PENDING)
2. Update to PROCESSING ✅
3. Try to update to PENDING ❌ (invalid transition)

## 👨‍💻 Author

**Rodrigo Bellanti**
- GitHub: [@RodrigoBellanti](https://github.com/rodrigobellanti)
- LinkedIn: [Rodrigo Bellanti](https://linkedin.com/in/rodrigo-bellanti)
- Email: rodrigobellanti@gmail.com

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 🙏 Acknowledgments

Built as a demonstration of modern Spring Boot development practices, including:
- Clean Architecture principles
- Domain-Driven Design concepts
- RESTful API best practices
- Comprehensive error handling
- Production-ready patterns

**Note:** This is a portfolio/learning project showcasing enterprise Java development skills. Not intended for production use without additional security hardening, monitoring, and testing.