# Database Schema Design (ERD)

This document outlines the database schema for the Smart Clinic Management System.

## Text-based ERD

```
+-----------+       +-----------------+       +-----------+
|  Doctor   |       |   Appointment   |       |  Patient  |
+-----------+       +-----------------+       +-----------+
| doctor_id (PK) |----<| doctor_id (FK)  |       | patient_id (PK) |
| name      |       | appointment_id (PK)|------>| patient_id (FK) |
| email     |       | appointment_time|       | name      |
| phone_number|     +-----------------+       | email     |
| speciality|                                 | phone_number|
| available_times|                            +-----------+
+-----------+                                       |
     |                                              |
     |                                              |
     |                                              |
+----v------+       +-----------------+             |
|Prescription|     |                 |             |
+-----------+       +-----------------+             |
|prescription_id (PK)|                               |
| doctor_id (FK)  |>----------------------------------+
| patient_id (FK) |
| details         |
+-----------------+
```

## Table Definitions

### 1. `doctors` Table
Stores information about the doctors in the clinic.

-   **doctor_id**: `BIGINT`, PRIMARY KEY, AUTO_INCREMENT
-   **name**: `VARCHAR(255)`, NOT NULL
-   **email**: `VARCHAR(255)`, NOT NULL, UNIQUE
-   **password**: `VARCHAR(255)`, NOT NULL
-   **phone_number**: `VARCHAR(20)`
-   **speciality**: `VARCHAR(100)`, NOT NULL

### 2. `doctor_available_times` Table (Element Collection)
Stores the available time slots for each doctor. This is managed by JPA's `@ElementCollection`.

-   **doctor_doctor_id**: `BIGINT`, FOREIGN KEY (references `doctors.doctor_id`)
-   **available_times**: `VARCHAR(255)`

### 3. `patients` Table
Stores information about the patients.

-   **patient_id**: `BIGINT`, PRIMARY KEY, AUTO_INCREMENT
-   **name**: `VARCHAR(255)`, NOT NULL
-   **email**: `VARCHAR(255)`, NOT NULL, UNIQUE
-   **password**: `VARCHAR(255)`, NOT NULL
-   **phone_number**: `VARCHAR(20)`

### 4. `appointments` Table
Stores information about scheduled appointments.

-   **appointment_id**: `BIGINT`, PRIMARY KEY, AUTO_INCREMENT
-   **doctor_id**: `BIGINT`, FOREIGN KEY (references `doctors.doctor_id`)
-   **patient_id**: `BIGINT`, FOREIGN KEY (references `patients.patient_id`)
-   **appointment_time**: `DATETIME`, NOT NULL

### 5. `prescriptions` Table
Stores prescription details provided by doctors to patients.

-   **prescription_id**: `BIGINT`, PRIMARY KEY, AUTO_INCREMENT
-   **doctor_id**: `BIGINT`, FOREIGN KEY (references `doctors.doctor_id`)
-   **patient_id**: `BIGINT`, FOREIGN KEY (references `patients.patient_id`)
-   **details**: `TEXT`, NOT NULL
-   **date_created**: `DATETIME`, NOT NULL

## Relationships

-   **Doctor to Appointment**: One-to-Many (`1..*`)
    -   One `Doctor` can have many `Appointments`.
-   **Patient to Appointment**: One-to-Many (`1..*`)
    -   One `Patient` can have many `Appointments`.
-   **Doctor to Prescription**: One-to-Many (`1..*`)
    -   One `Doctor` can write many `Prescriptions`.
-   **Patient to Prescription**: One-to-Many (`1..*`)
    -   One `Patient` can have many `Prescriptions`.
