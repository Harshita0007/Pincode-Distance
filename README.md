# Pincode Distance & Route API

This project provides a RESTful API to calculate the distance, duration, and detailed driving route between two Indian pincodes using the Google Maps Directions API. The result includes a human-readable step-by-step route.

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL
- Spring Cache
- Google Maps API
- Swagger

---

## Setup Instructions

### 1. Configure Environment

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pincodeapi
    username: your_db_user
    password: your_db_password

google:
  api:
    key: YOUR_GOOGLE_API_KEY
````

Make sure the Google API Key has:

* Directions API
* Geocoding API

---

### 2. Build & Run

```bash
./mvnw clean spring-boot:run
```

---

## API Usage

### GET /api/route

```http
GET /api/route?pincode1=201301&pincode2=201305
```

### Sample Response:

```json
{
  "id": 1,
  "pincode1": "201301",
  "pincode2": "201305",
  "distance": "16.3 km",
  "duration": "26 mins",
  "steps": [
    {
      "instruction": "Head northeast toward Sector 19 Rd Restricted usage road",
      "distance": "83 m",
      "duration": "1 min"
    }
  ]
}
```

---

## Swagger UI

Visit:

* [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* or [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


