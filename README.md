# Travel & Repeat Backend Rest API

## 1. Overview
Travel Agency dedicated to an Agent specialized and certified in Disney and Universal Studios.

`Travel & Repeat` Brand

`Eva Elizabeth` Travel agent

`Archer Travel` Main agency, licenses and external platforms with multiple providers

Products:
- Tour travels
- Tickets for theme parks
- Concert Event tickets
- Cruises
- Hotels and reservations
- Disney and Universal studios packages

## 2. Prerequisites
- Java 17 or higher
- Apache Maven 3.8+
- PostgreSQL 16 (for local development)
- Docker & Docker Compose (for containerized setup)
- Git

## 3. Quick Start
### Environments to run the project
- Local Development (dev)
- Docker Compose (dev)
- production (prod)
### Option 1: Local Development
1. Clone the repository

   git clone <https://github.com/travelandrepeatdev/travelandrepeat-be.git>

   cd travelandrepeat-be
2. Configure environment
    - Set environment variables, `application-dev.yml` automatically take these vars
3. Run PostgreSQL Dockerized (Only tested pointing to db docker container)
4. Build and run with IntelliJ

### Option 2: Docker Compose
1. Configure docker-compose.yml  with the env vars see dev env shared secrets <https://lastpass.com>
    
2. `docker-compose up -d`

Docker postgres db -> run the init scripts and mount volume for db

Docker api -> starts and mount the volumes

API will be available at: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

## 4. Environment Variables
| Variable | Description |                Example                 |
|----------|-------------|:--------------------------------------:|
| SPRING_PROFILES_ACTIVE | Active profile (dev/prod) |                  dev                   |
| DB_URL | PostgreSQL connection URL | jdbc:postgresql://localhost:5432/tyrdb |
| DB_USER | Database user |                postgres                |
| DB_PASS | Database password |                 secret                 |
| MAIL_HOST | SMTP server |             smtp.gmail.com             |
| MAIL_QUOTATION_FROM | Email sender |      see dev shared secrets lastpass.com      |
| MAIL_PASSWORD | Email password/app token |  see dev shared secrets lastpass.com   |
| MAIL_AGENT_TO | Agent email recipient |         set yours for testing          |
| RECAPTCHA_SECRET | Google reCAPTCHA secret key |  see dev shared secrets lastpass.com   |
| BANXICO_TOKEN | Banco de México API token |  see dev shared secrets lastpass.com   |
| JWT_SECRET | JWT signing secret |  see dev shared secrets lastpass.com   |

## 5. Security
### Authentication & Authorization
- JWT (JSON Web Tokens) for API authentication
- Spring Security for method-level authorization
- Role-based access control (RBAC) with granular permissions
- Password hashing using bcrypt
### External Security Integrations
- **Google reCAPTCHA V3**: Prevents bot abuse on public forms
- **HTTPS**: All traffic encrypted (443)
- **SMTP Gmail**: Secure email delivery with app tokens (not passwords)

## 6. Notes:
### Architecture design
Monolithic

### Technologies:
- Java
- Spring Framework
- Maven
- Postgres database (Supabase for prod env)
- OpenAPI with Swagger UI
- Docker
- VPS Ubuntu Server
- Nginx
- Grafana
- Github Actions
- Let's Encrypt certificate

### Project structure
📦 src/main/java/com/travelandrepeat/api  
┣ 📂 controller → REST endpoints  
┣ 📂 service → business logic  
┣ 📂 repository → database layer  
┣ 📂 entity → database mapped entities  
┣ 📂 dto → request/response models  
┣ 📂 utils → some tools or code utilities  
┗ 📂 config → application configuration

## 7. Agreement and additional information →
The property of this project and its code its own by `Travel & Repeat` and the `developer` who built it, the developer itself does not have any relation with other brands or companies mentioned in the document or technology used for this project

Contact info:

`Sergio A. Alfaro`

Software developer email: `travelandrepeatdev@gmail.com`