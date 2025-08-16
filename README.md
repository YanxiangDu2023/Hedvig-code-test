# Hedvig Insurance Code Test

A simplified insurance policy management service built with **Spring Boot** and **Kotlin**. It exposes REST endpoints to create/update policies and query premiums. Database schema is managed by **Liquibase** and runs on **H2**.

---

## Features

* **Insurance & Policy**: one `Insurance` (per `personalNumber`) can have many `Policy` rows.
* **Premium calculation**: derived from postal code prefix.
* **Read/Write separation**: command service for mutations, query service for reads.
* **Migrations**: Liquibase SQL changelog.
* **Tests**: JUnit 5 + Mockito + MockMvc.

---

## Tech Stack

* Kotlin, Spring Boot
* Spring Web, Spring Data JPA
* H2 (in‑memory), Liquibase
* Maven, JUnit 5, Mockito, MockMvc

---

## Project Structure

```
src
├─ main
│  ├─ kotlin/com.hedvig.policies
│  │  ├─ controller/            # REST controllers
│  │  ├─ dto/                   # request/response DTOs
│  │  ├─ model/                 # JPA entities (Insurance, Policy)
│  │  ├─ repository/            # Spring Data repositories
│  │  └─ service/               # command/query services + PremiumCalculator
│  └─ resources
│     ├─ db.changelog/migrations/1-initial-migration.sql
│     └─ application.properties
└─ test/kotlin/com.hedvig.policies
   ├─ Controller/               # MockMvc tests
   └─ Service/                  # Unit tests
```

---

## Run the app

```bash
./mvnw spring-boot:run
```

The app starts on **[http://localhost:8080](http://localhost:8080)**.

### Test the API (Swagger UI)

Open **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)** and try the endpoints interactively.

### Inspect the database (H2 Console)

Open **[http://localhost:8080/h2-console/](http://localhost:8080/h2-console/)** and use:

* **JDBC URL**: `jdbc:h2:mem:testdb`
* **User Name**: `sa`
* **Password**: *(leave empty)*

Example queries:

```sql
SELECT * FROM INSURANCE;
SELECT * FROM POLICY ORDER BY INSURANCE_ID, START_DATE;
```

---

## API Endpoints

### Create insurance

```
POST /insurances
Content-Type: application/json
```

Request body:

```json
{
  "personalNumber": "19900101-1234",
  "address": "Kungsgatan 16",
  "postalCode": "11135",
  "startDate": "2025-01-01"
}
```

### Update / add policy to an insurance

```
POST /insurances/{insuranceId}/policies
Content-Type: application/json
```

Request body:

```json
{
  "address": "Drottninggatan 22",
  "postalCode": "11151",
  "startDate": "2025-06-01"
}
```

### List insurances by personal number

```
GET /insurances?personalNumber=19900101-1234
```

### List policies active on a given date for a personal number

```
GET /insurances/policies?personalNumber=19900101-1234&date=2025-03-01
```

### Get the policy active at a date for a specific insurance

```
GET /insurances/{insuranceId}/policy-at?date=2025-03-01
```

### Get current policies (or at a specific date)

```
GET /insurances/current?personalNumber=19900101-1234[&date=2025-03-01]
```

Response is a list of `CurrentPolicyDto` with the active policy per insurance.

### Get total premium over a date range

```
GET /insurances/premium/total?personalNumber=19900101-1234&from=2025-01-01&to=2025-06-30
```

Returns `PremiumTotalDto` with the summed monthly premium across overlapping months.

---

## Database Schema

**insurance**

* `id` (PK)
* `personal_number` (UNIQUE)

**policy**

* `id` (PK)
* `insurance_id` (FK → insurance.id)
* `address`
* `postal_code`
* `start_date`
* `end_date` (nullable)
* `premium`

Liquibase runs automatically on startup and creates these tables.

---

## Run tests

```bash
./mvnw test
```

Covers premium calculation, service logic (create/update), and controller endpoints.

---

## Notes

* Premium rule (example): postal codes starting with `11` → 100.00; otherwise 150.00.
* H2 is in-memory by default; restart resets data. Use the H2 console for quick inspection.
