# DAO Contract

## Purpose

The Data Access Object (DAO) layer is responsible for communicating with the MySQL database.

Each DAO handles database operations for one entity.

The DAOs use `DatabaseConnection` to obtain a JDBC connection and should not contain GUI code.

---

## DatabaseConnection

`DatabaseConnection` is shared by all DAOs.

### Responsibility

- Establish a connection to the `hospital` MySQL database.
- Provide database connections to DAO classes.
- Handle database connection details.

### Method

- getConnection(password : String) : Connection
  - Establishes and returns a connection to the database.
  - Throws `SQLException` if the connection cannot be established.

---

# PatientDAO

Handles database operations related to `Patient`.

### Methods

- addPatient(patient : Patient) : boolean
  - Inserts a new patient into the `Patient` table.
  - Returns `true` if the patient is successfully added, otherwise `false`.

- findPatient(patientId : String) : Patient
  - Finds a patient using their patient ID.
  - Returns the corresponding `Patient` object.
  - Returns `null` if the patient is not found.

- updatePatient(patient : Patient) : boolean
  - Updates the patient's information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deletePatient(patientId : String) : boolean
  - Deletes the patient with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getMedicalHistory(patientId : String) : List<Consultation>
  - Retrieves the consultations associated with the patient.
  - Returns a list of consultations.

- getAppointments(patientId : String) : List<Appointment>
  - Retrieves the appointments associated with the patient.
  - Returns a list of appointments.

- getPrescriptions(patientId : String) : List<Prescription>
  - Retrieves the prescriptions associated with the patient.
  - Returns a list of prescriptions.

---

# DoctorDAO

Handles database operations related to `Doctor`.

### Methods

- addDoctor(doctor : Doctor) : boolean
  - Inserts a new doctor into the `Doctor` table.
  - Returns `true` if the doctor is successfully added, otherwise `false`.

- findDoctor(doctorId : String) : Doctor
  - Finds a doctor using their doctor ID.
  - Returns the corresponding `Doctor` object.
  - Returns `null` if the doctor is not found.

- updateDoctor(doctor : Doctor) : boolean
  - Updates the doctor's information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deleteDoctor(doctorId : String) : boolean
  - Deletes the doctor with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getAppointments(doctorId : String) : List<Appointment>
  - Retrieves the appointments associated with the doctor.
  - Returns a list of appointments.

---

# AppointmentDAO

Handles database operations related to `Appointment`.

### Methods

- addAppointment(appointment : Appointment) : boolean
  - Inserts a new appointment into the `Appointment` table.
  - Returns `true` if the appointment is successfully added, otherwise `false`.

- findAppointment(appointmentId : String) : Appointment
  - Finds an appointment using its appointment ID.
  - Returns the corresponding `Appointment` object.
  - Returns `null` if the appointment is not found.

- updateAppointment(appointment : Appointment) : boolean
  - Updates the appointment information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deleteAppointment(appointmentId : String) : boolean
  - Deletes the appointment with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getAppointmentsByPatient(patientId : String) : List<Appointment>
  - Retrieves appointments belonging to a specific patient.
  - Returns a list of appointments.

- getAppointmentsByDoctor(doctorId : String) : List<Appointment>
  - Retrieves appointments belonging to a specific doctor.
  - Returns a list of appointments.

---

# ConsultationDAO

Handles database operations related to `Consultation`.

### Methods

- addConsultation(consultation : Consultation) : boolean
  - Inserts a new consultation into the `Consultation` table.
  - Returns `true` if the consultation is successfully added, otherwise `false`.

- findConsultation(consultationId : String) : Consultation
  - Finds a consultation using its consultation ID.
  - Returns the corresponding `Consultation` object.
  - Returns `null` if the consultation is not found.

- updateConsultation(consultation : Consultation) : boolean
  - Updates the consultation information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deleteConsultation(consultationId : String) : boolean
  - Deletes the consultation with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getConsultationsByPatient(patientId : String) : List<Consultation>
  - Retrieves consultations belonging to a specific patient.
  - Returns a list of consultations.

- getConsultationsByDoctor(doctorId : String) : List<Consultation>
  - Retrieves consultations belonging to a specific doctor.
  - Returns a list of consultations.

---

# PrescriptionDAO

Handles database operations related to `Prescription`.

### Methods

- addPrescription(prescription : Prescription) : boolean
  - Inserts a new prescription into the `Prescription` table.
  - Returns `true` if the prescription is successfully added, otherwise `false`.

- findPrescription(prescriptionId : String) : Prescription
  - Finds a prescription using its prescription ID.
  - Returns the corresponding `Prescription` object.
  - Returns `null` if the prescription is not found.

- updatePrescription(prescription : Prescription) : boolean
  - Updates the prescription information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deletePrescription(prescriptionId : String) : boolean
  - Deletes the prescription with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getPrescriptionsByPatient(patientId : String) : List<Prescription>
  - Retrieves prescriptions belonging to a specific patient.
  - Returns a list of prescriptions.

- getPrescriptionsByDoctor(doctorId : String) : List<Prescription>
  - Retrieves prescriptions belonging to a specific doctor.
  - Returns a list of prescriptions.

---

# BillingDAO

Handles database operations related to `Billing`.

### Methods

- addBill(bill : Billing) : boolean
  - Inserts a new bill into the `Billing` table.
  - Returns `true` if the bill is successfully added, otherwise `false`.

- findBill(billId : String) : Billing
  - Finds a bill using its bill ID.
  - Returns the corresponding `Billing` object.
  - Returns `null` if the bill is not found.

- updateBill(bill : Billing) : boolean
  - Updates the billing information in the database.
  - Returns `true` if the update is successful, otherwise `false`.

- deleteBill(billId : String) : boolean
  - Deletes the bill with the specified ID.
  - Returns `true` if the deletion is successful, otherwise `false`.

- getBillByConsultation(consultationId : String) : Billing
  - Retrieves the bill associated with a consultation.
  - Returns the corresponding `Billing` object.
  - Returns `null` if no bill exists for the consultation.