# Skill 4 — Token-Based Authentication for Banking API

This project implements the Skill 4 requirements from the SOA Programming and Microservices workbook:
- Generate a JWT token after a successful login.
- Protect `GET /account/details`.
- Validate token signature and expiration.
- Reject missing, invalid, and expired tokens.
- Test valid and invalid access scenarios.

## Requirements
- Java 21 (or compatible Java version supported by the Spring Boot version)
- Maven 3.8+
- No MySQL or SQL password required. Demo credentials are held in local application configuration.

## Run the application
Open a terminal in the folder containing `pom.xml`:

```powershell
mvn spring-boot:run
```

The server runs at `http://localhost:8080`.

## Demo login credentials
- Username: `bankuser`
- Password: `BankPass123!`

These are demonstration-only credentials. Do not use them for a real banking system.

## Test 1 — Login and get a token
In a second PowerShell terminal:

```powershell
$body = @{ username = "bankuser"; password = "BankPass123!" } | ConvertTo-Json
$response = Invoke-RestMethod -Method Post -Uri http://localhost:8080/login -ContentType "application/json" -Body $body
$token = $response.accessToken
$response
```

The response includes `tokenType`, `accessToken`, and `expiresInSeconds` (default 900 seconds / 15 minutes).

## Test 2 — Access the protected account endpoint with a valid token

```powershell
Invoke-RestMethod -Method Get -Uri http://localhost:8080/account/details -Headers @{ Authorization = "Bearer $token" }
```

Expected: account demo data and `Authenticated access granted`.

## Test 3 — Try without a token

```powershell
try {
  Invoke-RestMethod -Method Get -Uri http://localhost:8080/account/details
} catch {
  Write-Host "HTTP status:" ([int]$_.Exception.Response.StatusCode)
}
```

Expected: HTTP 401 Unauthorized.

## Test 4 — Try an invalid token

```powershell
try {
  Invoke-RestMethod -Method Get -Uri http://localhost:8080/account/details -Headers @{ Authorization = "Bearer invalid.token.value" }
} catch {
  Write-Host "HTTP status:" ([int]$_.Exception.Response.StatusCode)
}
```

Expected: HTTP 401 Unauthorized.

## Test 5 — Invalid login

```powershell
$body = @{ username = "bankuser"; password = "wrong-password" } | ConvertTo-Json
try {
  Invoke-RestMethod -Method Post -Uri http://localhost:8080/login -ContentType "application/json" -Body $body
} catch {
  Write-Host "HTTP status:" ([int]$_.Exception.Response.StatusCode)
}
```

Expected: HTTP 401 Unauthorized.

## Security notes
- The token is signed and its signature and expiration are checked on protected requests.
- Tokens are stateless; the server does not create an HTTP session.
- The default JWT secret is for local learning only. Override `JWT_SECRET` with a long random secret outside a demo.
- Do not commit real secrets or production credentials to GitHub.
- The account response contains fictitious sample data, not real financial information.

## Workbook coverage
- Step 1: `POST /login` generates a JWT after successful login.
- Step 2: `GET /account/details` is protected.
- Step 3: JWT signature and expiry are validated.
- Step 4: Invalid/expired tokens do not authenticate the request.
- Step 5: README provides commands for valid, missing, invalid token, and invalid login scenarios.
