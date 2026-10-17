
# Database Design

## 1. Overview

The Hospital Management System uses a MySQL database named `hospital` to store patient records, doctor records, appointments, consultations, prescriptions, billing information, and user accounts.

The database contains seven tables:

- `Patient`
- `Doctor`
- `Appointment`
- `Consultation`
- `Prescription`
- `Billing`
- `UserAccount`

Primary keys uniquely identify records, while foreign keys establish relationships between related tables. The database uses `AUTO_INCREMENT` for primary keys that are generated automatically.

---

## 2. Patient

Stores personal and contact information for patients.

### Columns

- `patient_id` : INT (Primary Key, AUTO_INCREMENT)
- `name` : VARCHAR(100)
- `age` : INT
- `phone` : VARCHAR(100)
- `address` : VARCHAR(100)
- `email` : VARCHAR(100)

### Constraints

- `patient_id` is the primary key.
- The patient ID is generated automatically by the database.
- Patient records can be referenced by appointments, consultations, prescriptions, bills, and user accounts.

---

## 3. Doctor

Stores personal information and professional details of doctors.

### Columns

- `doctor_id` : INT (Primary Key, AUTO_INCREMENT)
- `name` : VARCHAR(100)
- `age` : INT
- `phone` : VARCHAR(100)
- `address` : VARCHAR(100)
- `email` : VARCHAR(100)
- `specialization` : VARCHAR(20)

### Constraints

- `doctor_id` is the primary key.
- The doctor ID is generated automatically by the database.
- Doctor records can be referenced by appointments, consultations, prescriptions, and user accounts.

---

## 4. Appointment

Stores appointments scheduled between patients and doctors.

### Columns

- `appointment_id` : INT (Primary Key, AUTO_INCREMENT)
- `patient_id` : INT (Foreign Key → Patient.patient_id)
- `doctor_id` : INT (Foreign Key → Doctor.doctor_id)
- `date` : DATE
- `time` : TIME
- `status` : VARCHAR(50)

### Constraints

- `appointment_id` is the primary key.
- `patient_id` references `Patient.patient_id`.
- `doctor_id` references `Doctor.doctor_id`.

### Description

Each appointment is associated with one patient and one doctor. A patient or doctor can have multiple appointments.

The `status` column stores the appointment's current state.

---

## 5. Consultation

Stores clinical information recorded when a patient receives a consultation from a doctor.

### Columns

- `consultation_id` : INT (Primary Key, AUTO_INCREMENT)
- `patient_id` : INT (Foreign Key → Patient.patient_id, NOT NULL)
- `doctor_id` : INT (Foreign Key → Doctor.doctor_id, NOT NULL)
- `consultation_date` : DATE (NOT NULL)
- `consultation_time` : TIME (NOT NULL)
- `diagnosis` : TEXT
- `notes` : TEXT
- `prescription_id` : INT (Foreign Key → Prescription.prescription_id, UNIQUE)

### Constraints

- `consultation_id` is the primary key.
- `patient_id` references `Patient.patient_id`.
- `doctor_id` references `Doctor.doctor_id`.
- `prescription_id` references `Prescription.prescription_id`.
- The `UNIQUE` constraint on `prescription_id` prevents the same prescription from being associated with multiple consultations.
- `prescription_id` can be NULL, allowing a consultation without a prescription.

### Description

Each consultation belongs to one patient and one doctor. A patient and a doctor can have multiple consultations.

The diagnosis and notes are stored using `TEXT` to accommodate longer clinical information.

---

## 6. Prescription

Stores medication and treatment instructions prescribed to patients.

### Columns

- `prescription_id` : INT (Primary Key, AUTO_INCREMENT)
- `patient_id` : INT (Foreign Key → Patient.patient_id)
- `doctor_id` : INT (Foreign Key → Doctor.doctor_id)
- `medication` : VARCHAR(100)
- `dosage` : VARCHAR(100)
- `frequency` : VARCHAR(100)
- `duration` : VARCHAR(100)
- `instructions` : VARCHAR(255)

### Constraints

- `prescription_id` is the primary key.
- `patient_id` references `Patient.patient_id`.
- `doctor_id` references `Doctor.doctor_id`.

### Description

Each prescription is associated with a patient and a doctor. A patient or doctor can have multiple prescriptions.

The prescription may also be associated with a consultation through `Consultation.prescription_id`.

---

## 7. Billing

Stores billing information for patients and their consultations.

### Columns

- `bill_id` : INT (Primary Key, AUTO_INCREMENT)
- `patient_id` : INT (Foreign Key → Patient.patient_id)
- `consultation_id` : INT (Foreign Key → Consultation.consultation_id)
- `amount` : DECIMAL(10,2)
- `date` : DATE
- `status` : VARCHAR(50)

### Constraints

- `bill_id` is the primary key.
- `patient_id` references `Patient.patient_id`.
- `consultation_id` references `Consultation.consultation_id`.

### Description

Each bill can reference a patient and a consultation. The amount is stored using `DECIMAL(10,2)` to support monetary values with two decimal places.

The `status` column stores the payment state.

**Note:** `consultation_id` is not declared `UNIQUE` in the current schema. Therefore, the database permits multiple bills to reference the same consultation.

---

## 8. UserAccount

Stores login credentials, account roles, and associations with patient or doctor records.

### Columns

- `user_id` : INT (Primary Key, AUTO_INCREMENT)
- `username` : VARCHAR(50) (NOT NULL, UNIQUE)
- `password_hash` : VARCHAR(255) (NOT NULL)
- `role` : VARCHAR(20) (NOT NULL)
- `patient_id` : INT (UNIQUE, Foreign Key → Patient.patient_id)
- `doctor_id` : INT (UNIQUE, Foreign Key → Doctor.doctor_id)
- `is_active` : BOOLEAN (NOT NULL, DEFAULT TRUE)

### Constraints

- `user_id` is the primary key.
- `username` must be unique and cannot be NULL.
- `password_hash` cannot be NULL.
- `patient_id` references `Patient.patient_id`.
- `doctor_id` references `Doctor.doctor_id`.
- `patient_id` and `doctor_id` are individually unique, preventing the same patient or doctor from being linked to multiple accounts.
- `role` is restricted to `ADMIN`, `DOCTOR`, or `PATIENT` by a CHECK constraint.
- The `chk_user_link` CHECK constraint enforces role-specific associations:
  - An `ADMIN` account must have both `patient_id` and `doctor_id` set to NULL.
  - A `PATIENT` account must have a `patient_id` and a NULL `doctor_id`.
  - A `DOCTOR` account must have a `doctor_id` and a NULL `patient_id`.
- `is_active` indicates whether the account is active.

### Description

The `UserAccount` table supports role-based authentication for administrators, doctors, and patients.

Passwords are represented by stored password hashes rather than plain-text passwords.

---

## 9. Relationships

### Patient–Appointment

- One patient can have many appointments.
- Each appointment references one patient.

### Doctor–Appointment

- One doctor can have many appointments.
- Each appointment references one doctor.

### Patient–Consultation

- One patient can have many consultations.
- Each consultation references one patient.

### Doctor–Consultation

- One doctor can have many consultations.
- Each consultation references one doctor.

### Patient–Prescription

- One patient can have many prescriptions.
- Each prescription references one patient.

### Doctor–Prescription

- One doctor can have many prescriptions.
- Each prescription references one doctor.

### Consultation–Prescription

- A consultation can reference zero or one prescription.
- A prescription can be referenced by at most one consultation.
- The unique constraint on `Consultation.prescription_id` enforces this relationship.

### Patient–Billing

- One patient can be associated with many bills.
- Each bill references one patient.

### Consultation–Billing

- A consultation can be associated with multiple bills under the current schema.
- Each bill references one consultation when `consultation_id` is populated.
- The current schema does not enforce a one-to-one relationship between consultations and bills.

### Patient–UserAccount

- A patient can be associated with at most one user account.
- A patient-role account must reference a patient.

### Doctor–UserAccount

- A doctor can be associated with at most one user account.
- A doctor-role account must reference a doctor.

### Administrator–UserAccount

- Administrator accounts are not linked to patient or doctor records.
- The role and CHECK constraints distinguish administrator accounts from patient and doctor accounts.

---

## 10. Primary Keys and Foreign Keys

Primary keys uniquely identify records within each table.

| Table | Primary Key |
|---|---|
| Patient | `patient_id` |
| Doctor | `doctor_id` |
| Appointment | `appointment_id` |
| Consultation | `consultation_id` |
| Prescription | `prescription_id` |
| Billing | `bill_id` |
| UserAccount | `user_id` |

Foreign keys maintain referential integrity between related tables. They prevent a record from referencing a nonexistent parent record.

The schema does not explicitly define cascading delete or update actions. Consequently, the database uses MySQL's default foreign-key behavior.

---

## 11. Database Connection

The Java application connects to the `hospital` database through the shared `DatabaseConnection` class.

The connection settings are read from the local `config.properties` file. DAO classes use the returned JDBC connection to execute database operations.

This separation allows the application to keep database access logic in the persistence layer rather than placing SQL operations directly in the GUI classes.
