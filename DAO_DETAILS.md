
# DAO Contract

## 1. Purpose

The Data Access Object (DAO) layer is responsible for communication between the Java application and the MySQL database.

Each DAO handles database operations for a particular entity. The DAO layer separates database logic from the Swing graphical user interface.

The DAOs use `DatabaseConnection` to obtain JDBC connections and use SQL statements to retrieve, insert, update, or delete records.

---

## 2. DatabaseConnection

`DatabaseConnection` is shared by all DAO classes.

### Responsibilities

- Establish a connection to the `hospital` MySQL database.
- Read database credentials from the local `config.properties` file.
- Provide JDBC connections to DAO classes.

### Method

- `getConnection() : Connection`
  - Establishes and returns a JDBC connection to the MySQL database.
  - Uses the database URL `jdbc:mysql://localhost:3306/hospital`.
  - Reads `DB_USER` and `DB_PASSWORD` from `config.properties`.
  - Throws `SQLException` if a database connection error occurs.
  - Throws `IOException` if the configuration file cannot be read.

---

## 3. PatientDAO

Handles database operations related to `Patient`.

### Methods

- `addPatient(patient : Patient) : boolean`
  - Inserts a new patient into the `Patient` table.
  - Retrieves the generated patient ID and assigns it to the `Patient` object.
  - Returns `true` if the insertion succeeds; otherwise, `false`.

- `findPatient(patientId : int) : Patient`
  - Retrieves a patient using the specified patient ID.
  - Returns the corresponding `Patient` object or `null` if the patient is not found.

- `updatePatient(patient : Patient) : boolean`
  - Updates an existing patient's information.
  - Returns `true` if the update succeeds; otherwise, `false`.

- `deletePatient(patientId : int) : boolean`
  - Deletes the patient with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `getAppointments(patientId : int) : List<Appointment>`
  - Retrieves appointments associated with the specified patient.
  - Returns a list of appointments.

- `getMedicalHistory(patientId : int) : List<Consultation>`
  - Retrieves consultations associated with the specified patient.
  - Returns a list of consultations representing the patient's medical history.

- `getPrescriptions(patientId : int) : List<Prescription>`
  - Retrieves prescriptions associated with the specified patient.
  - Returns a list of prescriptions.

- `getAllPatients() : List<Patient>`
  - Retrieves all patient records from the `Patient` table.
  - Returns a list of `Patient` objects.

---

## 4. DoctorDAO

Handles database operations related to `Doctor`.

### Methods

- `addDoctor(doctor : Doctor) : boolean`
  - Inserts a new doctor into the `Doctor` table.
  - Retrieves the generated doctor ID and assigns it to the `Doctor` object.
  - Returns `true` if the insertion succeeds; otherwise, `false`.

- `addDoctorWithAccount(doctor : Doctor, username : String, password : String) : boolean`
  - Creates a doctor record and the associated doctor login account.
  - Associates the account with the newly generated doctor ID.
  - Uses a database transaction to create the doctor and account together.
  - Returns `true` if the operation succeeds; otherwise, `false`.
  - May throw an exception if an error occurs during the operation.

- `findDoctor(doctorId : int) : Doctor`
  - Retrieves a doctor using the specified doctor ID.
  - Returns the corresponding `Doctor` object or `null` if the doctor is not found.

- `updateDoctor(doctor : Doctor) : boolean`
  - Updates an existing doctor's information.
  - Returns `true` if the update succeeds; otherwise, `false`.

- `deleteDoctor(doctorId : int) : boolean`
  - Deletes the doctor with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `getAppointments(doctorId : int) : List<Appointment>`
  - Retrieves appointments associated with the specified doctor.
  - Returns a list of appointments.

- `getAllDoctors() : List<Doctor>`
  - Retrieves all doctor records from the `Doctor` table.
  - Returns a list of `Doctor` objects.

---

## 5. AppointmentDAO

Handles database operations related to `Appointment`.

### Methods

- `addAppointment(appointment : Appointment) : boolean`
  - Inserts a new appointment into the `Appointment` table.
  - Retrieves the generated appointment ID and assigns it to the `Appointment` object.
  - Returns `true` if the insertion succeeds; otherwise, `false`.

- `findAppointment(appointmentId : int) : Appointment`
  - Retrieves an appointment using the specified appointment ID.
  - Returns the corresponding `Appointment` object or `null` if it is not found.

- `updateAppointment(appointment : Appointment) : boolean`
  - Updates an existing appointment's information.
  - Returns `true` if the update succeeds; otherwise, `false`.

- `deleteAppointment(appointmentId : int) : boolean`
  - Deletes the appointment with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `getAppointmentsByPatient(patientId : int) : List<Appointment>`
  - Retrieves appointments belonging to a specific patient.
  - Returns a list of appointments.

- `getAppointmentsByDoctor(doctorId : int) : List<Appointment>`
  - Retrieves appointments assigned to a specific doctor.
  - Returns a list of appointments.

---

## 6. ConsultationDAO

Handles database operations related to `Consultation`.

### Methods

- `addConsultation(consultation : Consultation) : boolean`
  - Inserts a new consultation into the `Consultation` table.
  - Retrieves the generated consultation ID and assigns it to the `Consultation` object.
  - Stores the patient, doctor, consultation date and time, diagnosis, notes, and optional prescription reference.
  - Returns `true` if the insertion succeeds; otherwise, `false`.

- `findConsultation(consultationId : int) : Consultation`
  - Retrieves a consultation using the specified consultation ID.
  - Returns the corresponding `Consultation` object or `null` if it is not found.

- `updateConsultation(consultation : Consultation) : boolean`
  - Updates an existing consultation's information.
  - Returns `true` if the update succeeds; otherwise, `false`.

- `deleteConsultation(consultationId : int) : boolean`
  - Deletes the consultation with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `getConsultationsByPatient(patientId : int) : List<Consultation>`
  - Retrieves consultations belonging to a specific patient.
  - Returns a list of consultations.

- `getConsultationsByDoctor(doctorId : int) : List<Consultation>`
  - Retrieves consultations recorded by a specific doctor.
  - Returns a list of consultations.

---

## 7. PrescriptionDAO

Handles database operations related to `Prescription`.

### Methods

- `addPrescription(prescription : Prescription) : boolean`
  - Inserts a new prescription into the `Prescription` table.
  - Retrieves the generated prescription ID and assigns it to the `Prescription` object.
  - Returns `true` if the insertion succeeds; otherwise, `false`.

- `findPrescription(prescriptionId : int) : Prescription`
  - Retrieves a prescription using the specified prescription ID.
  - Returns the corresponding `Prescription` object or `null` if it is not found.

- `updatePrescription(prescription : Prescription) : boolean`
  - Updates an existing prescription's information.
  - Returns `true` if the update succeeds; otherwise, `false`.

- `deletePrescription(prescriptionId : int) : boolean`
  - Deletes the prescription with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `getPrescriptionsByPatient(patientId : int) : List<Prescription>`
  - Retrieves prescriptions associated with a specific patient.
  - Returns a list of prescriptions.

- `getPrescriptionsByDoctor(doctorId : int) : List<Prescription>`
  - Retrieves prescriptions associated with a specific doctor.
  - Returns a list of prescriptions.

---

## 8. BillingDAO

Handles database operations related to `Billing`.

### Methods

- `addBill(patientId : int, amount : double, status : String) : boolean`
  - Inserts a bill containing the patient ID, amount, and status into the `Billing` table.
  - Returns `true` if the insertion succeeds; otherwise, `false`.
  - The current implementation does not return the generated bill ID or accept a `Billing` object.

- `getAllBills() : List<Object[]>`
  - Retrieves bill records from the database.
  - Returns each bill as an `Object[]` containing the bill ID, patient ID, formatted amount, and status.
  - Returns a list of bill records.

- `deleteBill(billId : int) : boolean`
  - Deletes the bill with the specified ID.
  - Returns `true` if the deletion succeeds; otherwise, `false`.

- `updateBillStatus(billId : int, newStatus : String) : boolean`
  - Updates the status of the specified bill.
  - Returns `true` if the update succeeds; otherwise, `false`.

### Implementation Note

The current `addBill()` implementation inserts only `patient_id`, `amount`, and `status`. It does not explicitly insert `consultation_id` or `date`, nor does it assign the generated bill ID to a `Billing` object.

The `getAllBills()` method returns `Object[]` records rather than `Billing` objects.

---

## 9. UserAccountDAO

Handles database operations related to `UserAccount` and user authentication records.

### Methods

- `findByUsername(username : String) : UserAccount`
  - Retrieves a user account using the specified username.
  - Returns the corresponding `UserAccount` object or `null` if no account is found.
  - Retrieves account information including the username, password hash, role, associated patient or doctor ID, and active status.
  - May throw `SQLException` or `IOException`.

- `createAccount(username : String, password : String, role : UserRole, patientId : Integer, doctorId : Integer) : boolean`
  - Creates a user account with the specified username, password, and role.
  - Associates the account with a patient or doctor when applicable.
  - Validates the supplied account information and role-specific associations.
  - Returns `true` if the account is created successfully; otherwise, `false`.
  - May throw exceptions during account creation.

---

## 10. General DAO Guidelines

- All database operations should use `DatabaseConnection.getConnection()` to obtain a JDBC connection.
- Use `PreparedStatement` for parameterized SQL queries.
- Use try-with-resources to close database resources automatically.
- Database-generated IDs should be retrieved when required and assigned to the corresponding entity objects.
- DAOs should handle database access and should not contain Swing GUI code.
- GUI classes and authentication services should call the appropriate DAO methods rather than duplicating database logic.
- Database operations should return appropriate results so that the calling code can respond to success or failure.
- Where multiple related records must be created together, use a database transaction to preserve consistency.
