# Class Design

## HospitalManagementSystem

### Attributes
- patients : List<Patient>
- doctors : List<Doctor>
- appointments : List<Appointment>
- consultations : List<Consultation>
- bills : List<Billing>
- prescriptions : List<Prescription>

### Methods
- addPatient()
- addDoctor()
- bookAppointment()
- cancelAppointment()
- createConsultation()
- createPrescription()
- generateBill()
- findPatient()
- findDoctor()

## Person

### Attributes

- name : String
- age : int
- phone : String
- address : String
- email : String

### Methods

- getName() : String
- setName() : void
- getAge() : int
- setAge() : void
- getPhone() : String
- setPhone() : void
- getAddress() : String
- setAddress() : void
- getEmail() : String
- setEmail() : void

---

## Patient

### Extends

- Person

### Additional Attributes

- patientId : int
- nextId : static int

> **Note:** `nextId` is used internally to generate unique patient IDs.

### Additional Methods

- getPatientId() : int

- viewMedicalHistory() : List\<Consultation>
  - Returns the patient's medical history as a list of consultations.

- viewAppointments() : List\<Appointment>
  - Returns the patient's appointments as a list of appointments.

- viewPrescriptions() : List\<Prescription>
  - Returns the patient's prescriptions as a list of prescriptions.

---

## Doctor

### Extends

- Person

### Additional Attributes

- doctorId : int
- specialization : String
- nextId : static int

> **Note:** `nextId` is used internally to generate unique doctor IDs.

### Additional Methods

- getDoctorId() : int
- getSpecialization() : String
- setSpecialization() : void
- viewAppointments() : List<Appointment>
  - Returns the doctor's appointments as a list of appointments.

---

## Appointment

### Attributes

- appointmentId : String
- patient : Patient
- doctor : Doctor
- date : LocalDate
- time : LocalTime
- status : String
- nextId : static int

> **Note:** `nextId` is used internally to generate unique appointment IDs.

### Methods

- getAppointmentId() : String
- getPatient() : Patient
- getDoctor() : Doctor
- getDate() : LocalDate
- getTime() : LocalTime
- getStatus() : String
- cancelAppointment() : void
- rescheduleAppointment() : void
- completeAppointment() : void

---

## Consultation

### Attributes

- consultationId : int
- patient : Patient
- doctor : Doctor
- date : LocalDate
- time : LocalTime
- diagnosis : String
- notes : String
- prescription : Prescription
- nextId : static int

> **Note:** `nextId` is used internally to generate unique consultation IDs.

### Methods

- getConsultationId() : int
- getPatient() : Patient
- getDoctor() : Doctor
- getDate() : String
- getDiagnosis() : String
- getNotes() : String
- getPrescription() : Prescription
- setDiagnosis() : void
- setNotes() : void
- setPrescription() : void

---

## Billing

### Attributes

- billId : int
- patient : Patient
- consultation : Consultation
- amount : double
- date : String
- status : String

### Methods

- getBillId() : int
- getPatient() : Patient
- getConsultation() : Consultation
- getAmount() : double
- getDate() : String
- getStatus() : String
- setStatus() : void
- markAsPaid() : void

---

### Prescription

#### Attributes

- prescriptionId : int
- patientId : int
- doctorId : int
- consultationId : int
- medication : String
- dosage : String
- frequency : String
- duration : String
- instructions : String

#### Methods

- getPrescriptionId() : int
- getPatientId() : int
- getDoctorId() : int
- getConsultationId() : int
- getMedication() : String
- getDosage() : String
- getFrequency() : String
- getDuration() : String
- getInstructions() : String
- setMedication() : void
- setDosage() : void
- setFrequency() : void
- setDuration() : void
- setInstructions() : void