
# CDAC Hub - Backend 

This is the backend API for the CDAC Student Project Repository platform. It handles all the heavy lifting: user authentication, role-based access control (Student, Reviewer, Admin), file uploads, and the review workflow logic. 

### Tech Stack
* **Java 17** & **Spring Boot 3.2.x**
* **MySQL 8** (Database) + Spring Data JPA
* **Spring Security** (Google OAuth2 + JWT)
* **Lombok** (to keep the code clean)

### Core Features
* **Google Login:** Users log in via their Google accounts (OAuth2) and receive a JWT token for stateless authentication.
* **Role Management:** Auto-assigns "ADMIN" to the first registered user, and "STUDENT" to everyone else. Admins can upgrade users to "REVIEWER" or "ADMIN" via the web panel.
* **Review Workflow:** Projects go from `PENDING` -> `APPROVED`/`REJECTED`. Only approved projects are exposed to the public API.
* **File Handling:** Multipart file upload for project source code and documentation.

### Local Setup Instructions

1. **Clone the repo:**
   ```bash
   git clone <your-repo-url>
   cd cdachub

```

2. **Database Setup:**
Make sure you have MySQL running locally. Create a fresh database:
```sql
CREATE DATABASE cdachub;

```


3. **Configure Application Properties:**
Open `src/main/resources/application.properties` and update your MySQL credentials and Google OAuth keys. You'll need to create a project in the Google Cloud Console to get your Client ID and Secret.
```properties
spring.datasource.username=root
spring.datasource.password=your_password

spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET

```


4. **Run the Application:**
Run it from your IDE (like Eclipse or IntelliJ) or use Maven:
```bash
./mvnw spring-boot:run

```


The server will start on `http://localhost:8080`.



---
