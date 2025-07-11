# Developer Documentation: Pincode Route API

This document explains the architecture, components, and development flow of the Pincode Route API — a Spring Boot backend that calculates routes and directions between Indian PIN codes using the Google Maps API.

---

## Stack & Tools

* Spring Boot 3.x
* PostgreSQL for persistence
* Google Maps API (Geocoding + Directions)
* Lombok for boilerplate reduction
* Spring Cache (optional: Caffeine or Redis)
* Swagger UI for API docs (if configured)

---

## Package Structure

```bash
com.example.pincodeapi
├── controller          # API endpoint
├── model               # Entities and DTOs
├── client              # Google API integration
├── service             # Business logic
└── PincodeApiApplication.java
```

---

## API Endpoint

### GET /api/route

**Query Params:**

* `pincode1`: Source PIN code (e.g., `201301`)
* `pincode2`: Destination PIN code (e.g., `201305`)

**Response:**

```json
{
  "id": 1,
  "pincode1": "201301",
  "pincode2": "201305",
  "distance": "16.3 km",
  "duration": "26 mins",
  "steps": [
    {
      "instruction": "Head northeast toward Sector 19 Rd",
      "distance": "83 m",
      "duration": "1 min"
    },
    ...
  ]
}
```

---

## Key Classes

### RouteResponse.java

* JPA Entity representing one route
* Fields: `pincode1`, `pincode2`, `distance`, `duration`, `fullRouteJson`, `polyline`
* Stores JSON of simplified instructions for easy frontend rendering

### RouteStep.java

* POJO holding a single route step with clean `instruction`, `distance`, and `duration`

### RouteController.java

* Exposes REST endpoint `/api/route`
* Converts `fullRouteJson` into a parsed step-by-step readable list

### GoogleDirectionsClientImpl.java

* Calls Google Directions API with lat/lng from both pincodes
* Extracts and sanitizes HTML instructions into readable steps
* Stores the entire result into DB for caching and retrieval

---

## Configuration

**application.yml** (simplified)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pincode_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

google:
  api:
    key: GOOGLE_API_KEY

logging:
  level:
    org.springframework: info
```

---

## Developer Notes

* Uses `RestTemplate` for HTTP client
* Strips `<b>`, `<div>` tags from Google HTML responses using Jsoup
* Full polyline is stored for mapping
* `RouteStep[]` is serialized/deserialized as JSON string in DB

---

## Testing

* `/api/route?pincode1=201301&pincode2=201305` returns readable and saved data
* Ensure PostgreSQL is running and API key is active

---

## Need Help?

Contact the backend maintainer or check Google Maps API docs:

* Geocoding API: [https://developers.google.com/maps/documentation/geocoding](https://developers.google.com/maps/documentation/geocoding)
* Directions API: [https://developers.google.com/maps/documentation/directions](https://developers.google.com/maps/documentation/directions)