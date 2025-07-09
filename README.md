# EcommerceApp

A full-stack ecommerce platform with a Java Spring Boot backend, Angular frontend, and Microsoft SQL Server database. Containerized with Docker for easy deployment and development.

---

## Quick Start (with Docker Compose)

1. **Prerequisites:**
   - [Docker](https://www.docker.com/products/docker-desktop) installed
2. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd ecommerceApp
   ```
3. **Build & run everything:**
   ```sh
   docker-compose up --build
   ```
4. **Access the app:**
   - Frontend: [http://localhost:4200](http://localhost:4200)
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - SQL Server: `localhost:1433` (user: `sa`, password: see below)

---

## Project Structure

```
ecommerceApp/
├── backend/    # Spring Boot API (Java)
│   ├── src/    # Java source code (main & test)
│   ├── build.gradle  # Gradle build config
│   ├── Dockerfile    # Backend Docker image
│   ├── application.properties  # Backend config
│   └── db-init/      # DB initialization scripts (SQL)
├── frontend/   # Angular SPA
│   ├── src/    # Angular source code
│   ├── angular.json  # Angular CLI config
│   ├── Dockerfile    # Frontend Docker image
│   └── environment.ts  # Frontend config
├── docs/       # Documentation, DB scripts, API docs
├── docker-compose.yml # Orchestrates backend, frontend, and DB containers
└── ...
```

### Main Folders & Key Files
- **backend/**: Spring Boot REST API, business logic, and DB integration
  - `src/`: Java source code (main & test)
  - `build.gradle`: Gradle build config
  - `Dockerfile`: Containerizes backend
  - `application.properties`: Spring Boot config
  - `db-init/`: SQL scripts for DB/table/data initialization
- **frontend/**: Angular SPA
  - `src/`: Angular source code
  - `angular.json`: Angular CLI config
  - `Dockerfile`: Containerizes frontend
  - `environment.ts`: Angular environment config
- **docs/**: API docs, DB schema, developer notes
- **docker-compose.yml**: Orchestrates all containers/services

---

## Technologies Used
- **Backend:** Java 17+, Spring Boot, Gradle, JPA/Hibernate
- **Frontend:** Angular 16+, TypeScript, SCSS
- **Database:** SQL Server (Docker)
- **Other:** Docker, Docker Compose, JUnit/Mockito (testing), Node.js (frontend build)

---

## Getting Started: Step-by-Step

### Prerequisites
- [Docker](https://www.docker.com/products/docker-desktop) installed
- (Optional for local dev) Java 17+ (backend), Node.js 18+ & npm (frontend)

### Clone the Repository
```sh
git clone <repo-url>
cd ecommerceApp
```

### Build & Run Everything
```sh
docker compose up --build
```
- To reset all data and re-initialize, use:
  ```sh
  docker compose down -v && docker compose up --build
  ```

### Access the App
- **Frontend:** [http://localhost:4200](http://localhost:4200)
- **Backend API:** [http://localhost:8080](http://localhost:8080)
- **SQL Server:** `localhost:1433` (user: `sa`, password: see `docker-compose.yml`)

---

## How Docker Compose Works
- **Orchestrates:**
  - `backend` (Spring Boot API, port 8080)
  - `frontend` (Angular app, port 4200)
  - `db` (SQL Server, port 1433)
- **Builds and runs** backend and frontend containers from their respective Dockerfiles
- **Service dependencies:**
  - `backend` waits for `db` and DB initialization to be ready
  - `frontend` waits for `backend`
- **Volumes:**
  - SQL Server data persisted in `sqlserver_data`
  - Backend `/uploads` mapped for product images
- **Networking:**
  - All services share a Docker network, accessible by service name (e.g., `db`)
- **Ports:**
  - 8080 (backend), 4200 (frontend), 1433 (SQL Server) mapped to localhost

---

## Default Admin Account & Initial Data
On the **first run** (or after a full reset with `down -v`), the following are automatically created:
- **Admin Account:**
  - Email: `admin@admin.com`
  - Password: `admin123`
  - Name: `Admin 001`
  - Role: `ADMIN`
- **Category:**
  - Name: `Waxworks`
- **Products:**
  - 5 sample products, all linked to the `Waxworks` category (see `backend/db-init/init-db.sql` for details).

---

## UI Features by Role
- **ADMIN**
  - Full access to admin dashboard
  - Manage users, categories, products, and orders
  - View and edit all data
- **USER**
  - Browse products and categories
  - Manage own cart and orders
  - View own profile

---

## Notes
- The admin account and initial data are only created if the database is empty (first run or after `down -v`).
- To change the default admin password or initial data, edit `backend/db-init/init-db.sql` before starting the stack.
- The backend expects the database to be available at host `db` (see `docker-compose.yml`).
- All endpoints (except `/api/auth/*` and `/health`) require authentication. Admin endpoints require `ADMIN` role.

---

## Useful Commands
- **Stop all containers:**
  ```sh
  docker-compose down
  ```
- **Rebuild after code changes:**
  ```sh
  docker-compose up --build
  ```
- **View logs:**
  ```sh
  docker-compose logs -f
  ```

---

## Local Development (Optional)
- **Backend:** Java 17+ required to run/test outside Docker
- **Frontend:** Node.js 18+ & npm required to run/test outside Docker

---

For more details, see the `/docs` folder or open an issue. 