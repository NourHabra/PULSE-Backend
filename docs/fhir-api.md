# FHIR Patient API Documentation

This document provides examples of how to interact with the FHIR-compliant Patient endpoints using Postman.

## Base URL
```
http://localhost:8080/api/fhir
```

## Headers
```
Content-Type: application/fhir+json
Accept: application/fhir+json
```

## Endpoints

### 1. Create a New Patient (POST)
Creates a new patient record in FHIR format.

**URL**: `/Patient`

**Method**: `POST`

**Request Body**:
```json
{
    "resourceType": "Patient",
    "identifier": [
        {
            "system": "http://pulse.com/patients",
            "value": "P12345"
        }
    ],
    "active": true,
    "name": [
        {
            "use": "official",
            "family": "Smith",
            "given": ["John", "James"]
        }
    ],
    "telecom": [
        {
            "system": "phone",
            "value": "+1234567890",
            "use": "mobile"
        },
        {
            "system": "email",
            "value": "john.smith@email.com"
        }
    ],
    "gender": "male",
    "birthDate": "1974-12-25",
    "address": [
        {
            "use": "home",
            "type": "physical",
            "line": ["123 Main St"],
            "city": "Anytown",
            "state": "CA",
            "postalCode": "12345",
            "country": "USA"
        }
    ],
    "maritalStatus": {
        "coding": [
            {
                "system": "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus",
                "code": "M",
                "display": "Married"
            }
        ]
    }
}
```

**Expected Response** (200 OK):
```json
{
    "resourceType": "Patient",
    "id": "pat-1",
    "meta": {
        "versionId": "1",
        "lastUpdated": "2024-03-14T12:00:00Z",
        "profile": ["http://hl7.org/fhir/StructureDefinition/Patient"]
    },
    "identifier": [
        {
            "system": "http://pulse.com/patients",
            "value": "P12345"
        }
    ],
    "active": true,
    "name": [
        {
            "use": "official",
            "family": "Smith",
            "given": ["John", "James"]
        }
    ],
    "telecom": [
        {
            "system": "phone",
            "value": "+1234567890",
            "use": "mobile"
        },
        {
            "system": "email",
            "value": "john.smith@email.com"
        }
    ],
    "gender": "male",
    "birthDate": "1974-12-25",
    "address": [
        {
            "use": "home",
            "type": "physical",
            "line": ["123 Main St"],
            "city": "Anytown",
            "state": "CA",
            "postalCode": "12345",
            "country": "USA"
        }
    ],
    "maritalStatus": {
        "coding": [
            {
                "system": "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus",
                "code": "M",
                "display": "Married"
            }
        ]
    }
}
```

### 2. Get Patient by ID (GET)
Retrieves a specific patient's information using their FHIR ID.

**URL**: `/Patient/{id}`

**Method**: `GET`

**Example**: `GET /Patient/pat-1`

**Expected Response** (200 OK):
```json
{
    "resourceType": "Patient",
    "id": "pat-1",
    "meta": {
        "versionId": "1",
        "lastUpdated": "2024-03-14T12:00:00Z",
        "profile": ["http://hl7.org/fhir/StructureDefinition/Patient"]
    },
    "identifier": [
        {
            "system": "http://pulse.com/patients",
            "value": "P12345"
        }
    ],
    "active": true,
    "name": [
        {
            "use": "official",
            "family": "Smith",
            "given": ["John", "James"]
        }
    ],
    "telecom": [
        {
            "system": "phone",
            "value": "+1234567890",
            "use": "mobile"
        },
        {
            "system": "email",
            "value": "john.smith@email.com"
        }
    ],
    "gender": "male",
    "birthDate": "1974-12-25",
    "address": [
        {
            "use": "home",
            "type": "physical",
            "line": ["123 Main St"],
            "city": "Anytown",
            "state": "CA",
            "postalCode": "12345",
            "country": "USA"
        }
    ],
    "maritalStatus": {
        "coding": [
            {
                "system": "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus",
                "code": "M",
                "display": "Married"
            }
        ]
    }
}
```

### 3. Search Patients (GET)
Search for patients using various parameters.

**URL**: `/Patient`

**Method**: `GET`

**Query Parameters**:
- `family`: Patient's family name
- `given`: Patient's given name
- `birthdate`: Patient's birth date
- `gender`: Patient's gender
- `identifier`: Patient's identifier

**Example**: `GET /Patient?family=Smith&gender=male`

**Expected Response** (200 OK):
```json
{
    "resourceType": "Bundle",
    "type": "searchset",
    "total": 1,
    "entry": [
        {
            "resource": {
                "resourceType": "Patient",
                "id": "pat-1",
                "meta": {
                    "versionId": "1",
                    "lastUpdated": "2024-03-14T12:00:00Z",
                    "profile": ["http://hl7.org/fhir/StructureDefinition/Patient"]
                },
                "identifier": [
                    {
                        "system": "http://pulse.com/patients",
                        "value": "P12345"
                    }
                ],
                "active": true,
                "name": [
                    {
                        "use": "official",
                        "family": "Smith",
                        "given": ["John", "James"]
                    }
                ],
                "gender": "male",
                "birthDate": "1974-12-25"
            }
        }
    ]
}
```

## Testing Tips

1. Make sure the Spring Boot application is running before testing
2. Use environment variables in Postman for the base URL
3. All dates should be in ISO 8601 format (YYYY-MM-DD)
4. The FHIR ID format is "pat-{id}" where {id} is the internal patient ID
5. All responses will be in FHIR format with appropriate resource types and meta information

## Error Responses

### 404 Not Found
```json
{
    "resourceType": "OperationOutcome",
    "issue": [
        {
            "severity": "error",
            "code": "not-found",
            "details": {
                "text": "Patient not found"
            }
        }
    ]
}
```

### 400 Bad Request
```json
{
    "resourceType": "OperationOutcome",
    "issue": [
        {
            "severity": "error",
            "code": "invalid",
            "details": {
                "text": "Invalid request body"
            }
        }
    ]
}
``` 