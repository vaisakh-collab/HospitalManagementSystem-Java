# Class Design

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
- ...

### Additional Methods

- ...

---

## Doctor

### Extends

- Person

### Additional Attributes

- doctorId : int
- specialization : String
- ...

### Additional Methods

- ...

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
- date : String
- diagnosis : String
- notes : String
- prescription : Prescription

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