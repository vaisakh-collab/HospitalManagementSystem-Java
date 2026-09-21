# Database Design

## Patient

### Columns

- patient_id : INT (Primary Key, AUTO_INCREMENT)
- name : VARCHAR
- age : INT
- phone : VARCHAR
- address : VARCHAR
- email : VARCHAR

---

## Doctor

### Columns

- doctor_id : INT (Primary Key, AUTO_INCREMENT)
- name : VARCHAR
- age : INT
- phone : VARCHAR
- address : VARCHAR
- email : VARCHAR
- specialization : VARCHAR

---

## Appointment

### Columns

- appointment_id : INT (Primary Key, AUTO_INCREMENT)
- patient_id : INT (Foreign Key → Patient.patient_id)
- doctor_id : INT (Foreign Key → Doctor.doctor_id)
- date : DATE
- time : TIME
- status : VARCHAR

---

## Consultation

### Columns

- consultation_id : INT (Primary Key, AUTO_INCREMENT)
- patient_id : INT (Foreign Key → Patient.patient_id)
- doctor_id : INT (Foreign Key → Doctor.doctor_id)
- date : DATE
- time : TIME
- diagnosis : VARCHAR
- notes : VARCHAR
- prescription_id : INT (Foreign Key → Prescription.prescription_id, UNIQUE)

---

## Prescription

### Columns

- prescription_id : INT (Primary Key, AUTO_INCREMENT)
- patient_id : INT (Foreign Key → Patient.patient_id)
- doctor_id : INT (Foreign Key → Doctor.doctor_id)
- medication : VARCHAR
- dosage : VARCHAR
- frequency : VARCHAR
- duration : VARCHAR
- instructions : VARCHAR

---

## Billing

### Columns

- bill_id : INT (Primary Key, AUTO_INCREMENT)
- patient_id : INT (Foreign Key → Patient.patient_id)
- consultation_id : INT (Foreign Key → Consultation.consultation_id)
- amount : DECIMAL
- date : DATE
- status : VARCHAR

---

# Relationships

- One Patient can have many Appointments.
- One Doctor can have many Appointments.
- One Appointment belongs to one Patient.
- One Appointment belongs to one Doctor.

- One Patient can have many Consultations.
- One Doctor can have many Consultations.
- One Consultation belongs to one Patient.
- One Consultation belongs to one Doctor.

- One Patient can have many Prescriptions.
- One Doctor can have many Prescriptions.
- One Prescription belongs to one Patient.
- One Prescription belongs to one Doctor.

- One Consultation can have one Prescription.
- One Prescription belongs to at most one Consultation.

- One Patient can have many Bills.
- One Consultation can have one Bill.
- One Bill belongs to one Patient.
- One Bill belongs to one Consultation.