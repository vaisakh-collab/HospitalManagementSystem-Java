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
- addPatient(patient : Patient)
- addDoctor(doctor : Doctor)
- bookAppointment()
> WHAT???????
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
- setName(name : String) : void
- getAge() : int
- setAge(age : int) : void
- getPhone() : String
- setPhone(phone : String) : void
- getAddress() : String
- setAddress(address : String) : void
- getEmail() : String
- setEmail(email : String) : void

---

## Patient

### Extends

- Person

### Additional Attributes

- patientId : String
- nextId : static int

> **Note:** `nextId` is used internally to generate unique patient IDs.
> **Note:** Need to consider persistence of IDs during File Handling

### Additional Methods

- getPatientId() : String

- viewMedicalHistory() : List\<Consultation>
  - Returns the patient's medical history as a list of consultations.

- viewAppointments() : List\<Appointment>
  - Returns the patient's appointments as a list of appointments.

- viewPrescriptions() : List\<Prescription>
  - Returns the patient's prescriptions as a list of prescriptions.

> **Note:** YET TO CONSIDER HOW THESE DATA ARE STORED AND ACCESSED

---

## Doctor

### Extends

- Person

### Additional Attributes

- doctorId : String
- specialization : String
- nextId : static int

> **Note:** `nextId` is used internally to generate unique doctor IDs.
> **Note:** Need to consider persistence of IDs during File Handling

### Additional Methods

- getDoctorId() : String
- getSpecialization() : String
- setSpecialization(specialization : String) : void
- viewAppointments() : List\<Appointment>
  - Returns the doctor's appointments as a list of appointments.
> **Note:** YET TO CONSIDER HOW THIS DATA IS STORED AND ACCESSED
- isAvailable(requestedDate : LocalDate, requestedTime : LocalTime) : boolean
> Checks the appointments of the doctor assuming each to be of duration 20 min, and returns True if there is no overlap,

---

## Appointment

### Attributes

- appointmentId : String
- patient : Patient
- doctor : Doctor
- date : LocalDate
- time : LocalTime
- status : String
> **Note:** Consider an ENUM instead of String in case status needs to be inspected
- nextId : static int

> **Note:** `nextId` is used internally to generate unique appointment IDs.
> **Note:** Need to consider persistence of IDs during File Handling

### Methods

- getAppointmentId() : String
- getPatient() : Patient
- getDoctor() : Doctor
- getDate() : LocalDate
- getTime() : LocalTime
- getStatus() : String
- cancelAppointment() : void
- rescheduleAppointment(date : LocalDate, time: LocalTime) : void
- completeAppointment() : void

---

## Consultation

### Attributes

- consultationId : String
- patient : Patient
- doctor : Doctor
- date : LocalDate
- time : LocalTime
- diagnosis : String
- notes : String
- prescription : Prescription
- nextId : static int

> **Note:** `nextId` is used internally to generate unique consultation IDs.
> **Note:** Need to consider persistence of IDs during File Handling

### Methods

- getConsultationId() : String
- getPatient() : Patient
- getDoctor() : Doctor
- getDate() : LocalDate
- getTime() : LocalTime
- getDiagnosis() : String
- getNotes() : String
- getPrescription() : Prescription
- setDiagnosis(diagnosis : String) : void
- setNotes(notes : String) : void
- setPrescription(prescription : Prescription) : void

---

## Billing

### Attributes

- billId : String
- patient : Patient
- consultation : Consultation
- amount : double
- date : LocalDate
- time : LocalTime
- status : String
> **Note:** Consider an ENUM instead of String in case status needs to be inspected

- nextId : static int
> **Note:** `nextId` is used internally to generate unique billing IDs.
> **Note:** Need to consider persistence of IDs during File Handling

### Methods

- getBillId() : String
- getPatient() : Patient
- getConsultation() : Consultation
- getAmount() : double
- getDate() : LocalDate
- getTime() : LocalTime
- getStatus() : String
- markAsPaid() : void

---

## Prescription

#### Attributes

- prescriptionId : String
- patient : Patient
- doctor : Doctor
- medication : String
- dosage : String
- frequency : String
- duration : String
- instructions : String

- nextId : static int
> **Note:** `nextId` is used internally to generate unique prescription IDs.
> **Note:** Need to consider persistence of IDs during File Handling

#### Methods

- getPrescriptionId() : String
- getPatient() : Patient
- getDoctor() : Doctor
- getMedication() : String
- getDosage() : String
- getFrequency() : String
- getDuration() : String
- getInstructions() : String
- setMedication(medication : String) : void
- setDosage(dosage : String) : void
- setFrequency(frequency : String) : void
- setDuration(duration : String) : void
- setInstructions(instructions : String) : void