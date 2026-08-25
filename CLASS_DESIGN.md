# Class Design

## Person

### Attributes
- name : String
- age : int
- phone : String
- address : String
- email : String

### Methods
- getName()
- setName()
- getAge()
- setAge()
- getPhone()
- setPhone()
- getAddress()
- setAddress()
- getEmail()
- setEmail()

---

## Patient

### Extends
- Person

### Additional Attributes
- patientId : int
- ...

### Additional Methods

---

## Doctor

### Extends
- Person

### Additional Attributes
- doctorId : int
- specialization : String
- ...

### Additional Methods

---

## Appointment

### Attributes
- appointmentId : int
- patient : Patient
- doctor : Doctor
- date : LocalDate
- time : LocalTime
- status : String

### Methods
- getAppointmentId()
- getPatient()
- getDoctor()
- getDate()
- getTime()
- getStatus()
- setPatient()
- setDoctor()
- setDate()
- setTime()
- setStatus()
- cancel()
- reschedule()
- complete()

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
- getConsultationId()
- getPatient()
- getDoctor()
- getDate()
- getDiagnosis()
- getNotes()
- getPrescription()

- setDiagnosis()
- setNotes()
- setPrescription()

---

## Billing

## Attributes
- billId : int
- patient : Patient
- consultation : Consultation
- amount : double
- date : String
- status : String

Methods:
- getBillId()
- getPatient()
- getConsultation()
- getAmount()
- getDate()
- getStatus()

- setStatus()
- markAsPaid()