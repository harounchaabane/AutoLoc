# AutoLoc — Rapport d'Exécution : Swagger OpenAPI, DTOs, Mappers et Validation

## 1. Dépendances ajoutées (`pom.xml`)

```xml
<!-- SpringDoc OpenAPI (Swagger UI) -->
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.8.8</version>
</dependency>

<!-- Validation Starter (Jakarta Bean Validation / Hibernate Validator) -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## 2. Vérification de l'accès à Swagger UI

* **URL Swagger UI :** `http://localhost:8000/swagger-ui.html` (redirige vers `http://localhost:8000/swagger-ui/index.html`)
* **URL OpenAPI v3 Docs :** `http://localhost:8000/v3/api-docs`
* **Code HTTP :** `200 OK`

```http
HTTP/1.1 302 Found
Location: /swagger-ui/index.html

HTTP/1.1 200 OK
Content-Type: text/html
```

---

## 3. Architecture des DTOs et Mappers

### DTOs créés
1. **Agence :**
   * `AgenceRequestDTO` : validations `@NotBlank`, `@Size`.
   * `AgenceResponseDTO` : enrichi avec `nombreEmployes` et `nombreVehicules`.
2. **Client :**
   * `ClientRequestDTO` : validations `@NotBlank`, `@Email`, `@Size`.
   * `ClientResponseDTO` : enrichi avec `dateInscription` et `nombreReservations`.
3. **Véhicule :**
   * `VehiculeRequestDTO` : validations `@NotBlank`, `@NotNull`, `@DecimalMin(value="0.0", inclusive=false)`, `agenceId`.
   * `VehiculeResponseDTO` : enrichi avec `agenceNom` et `agenceId`.
4. **Erreurs :**
   * `ErrorResponseDTO` : structure standardisée (`timestamp`, `status`, `error`, `message`, `path`, `validationErrors`).

### Mappers créés
* `AgenceMapper` : conversion `toEntity()`, `toResponseDTO()`, `updateEntityFromDTO()`.
* `ClientMapper` : conversion `toEntity()`, `toResponseDTO()`, `updateEntityFromDTO()`.
* `VehiculeMapper` : conversion `toEntity()`, `toResponseDTO()`, `updateEntityFromDTO()` avec résolution de la relation `Agence`.

---

## 4. Gestion centralisée des exceptions (`GlobalExceptionHandler`)

Les exceptions suivantes sont interceptées et formatées en `ErrorResponseDTO` :
* `ResourceNotFoundException` $\rightarrow$ **404 NOT_FOUND**
* `MethodArgumentNotValidException` (Bean Validation) $\rightarrow$ **400 BAD_REQUEST** avec `validationErrors: { champ: "message d'erreur" }`
* `IllegalArgumentException` $\rightarrow$ **400 BAD_REQUEST**
* `HttpMessageNotReadableException` (JSON malformé) $\rightarrow$ **400 BAD_REQUEST**
* `Exception` $\rightarrow$ **500 INTERNAL_SERVER_ERROR**

---

## 5. Traces d'Exécution & Tests des Endpoints

### Test 1 : Validation Bean Validation (400 Bad Request)
**Requête :**
```bash
POST /api/agences HTTP/1.1
Content-Type: application/json

{}
```
**Réponse :**
```json
{
  "timestamp": "2026-11-12T16:58:48.0035012",
  "status": 400,
  "error": "Validation Failed",
  "message": "La validation des donnees a echoue",
  "path": "/api/agences",
  "validationErrors": {
    "nom": "Le nom de l'agence est obligatoire",
    "ville": "La ville est obligatoire",
    "adresse": "L'adresse est obligatoire",
    "telephone": "Le telephone est obligatoire"
  }
}
```

### Test 2 : Création d'une Agence (201 Created)
**Requête :**
```bash
POST /api/agences HTTP/1.1
Content-Type: application/json

{
  "nom": "Agence Tunis Centre",
  "ville": "Tunis",
  "adresse": "Avenue Habib Bourguiba",
  "telephone": "+21671000000"
}
```
**Réponse :**
```json
{
  "idAgence": 1,
  "nom": "Agence Tunis Centre",
  "ville": "Tunis",
  "adresse": "Avenue Habib Bourguiba",
  "telephone": "+21671000000",
  "nombreEmployes": 0,
  "nombreVehicules": 0
}
```

### Test 3 : Validation Client (400 Bad Request)
**Requête :**
```bash
POST /api/clients HTTP/1.1
Content-Type: application/json

{
  "nom": "",
  "prenom": "Ali",
  "email": "invalid-email",
  "telephone": "",
  "numPermis": ""
}
```
**Réponse :**
```json
{
  "timestamp": "2026-11-12T17:09:10.5723567",
  "status": 400,
  "error": "Validation Failed",
  "message": "La validation des donnees a echoue",
  "path": "/api/clients",
  "validationErrors": {
    "nom": "Le nom est obligatoire",
    "email": "L'email doit etre valide",
    "telephone": "Le telephone est obligatoire",
    "numPermis": "Le numero de permis est obligatoire"
  }
}
```

### Test 4 : Création d'un Client (201 Created)
**Requête :**
```bash
POST /api/clients HTTP/1.1
Content-Type: application/json

{
  "nom": "Ben Salah",
  "prenom": "Mohamed",
  "email": "mohamed.bensalah@email.com",
  "telephone": "+21620123456",
  "numPermis": "TN-12345678"
}
```
**Réponse :**
```json
{
  "idClient": 1,
  "nom": "Ben Salah",
  "prenom": "Mohamed",
  "email": "mohamed.bensalah@email.com",
  "telephone": "+21620123456",
  "numPermis": "TN-12345678",
  "dateInscription": "2026-11-12",
  "nombreReservations": 0
}
```

### Test 5 : Création d'un Véhicule avec Agence associée (201 Created)
**Requête :**
```bash
POST /api/vehicules HTTP/1.1
Content-Type: application/json

{
  "immatriculation": "220-TN-4567",
  "marque": "Volkswagen",
  "modele": "Golf 8",
  "categorie": "BERLINE",
  "tarifJournalier": 120.00,
  "statut": "DISPONIBLE",
  "agenceId": 1
}
```
**Réponse :**
```json
{
  "idVehicule": 1,
  "immatriculation": "220-TN-4567",
  "marque": "Volkswagen",
  "modele": "Golf 8",
  "categorie": "BERLINE",
  "tarifJournalier": 120.00,
  "statut": "DISPONIBLE",
  "agenceNom": "Agence Tunis Centre",
  "agenceId": 1
}
```

### Test 6 : Exception Ressource Introuvable (404 Not Found)
**Requête :**
```bash
GET /api/vehicules/999 HTTP/1.1
```
**Réponse :**
```json
{
  "timestamp": "2026-11-12T17:10:48.2727048",
  "status": 404,
  "error": "Not Found",
  "message": "Vehicule introuvable avec l'identifiant 999",
  "path": "/api/vehicules/999"
}
```
