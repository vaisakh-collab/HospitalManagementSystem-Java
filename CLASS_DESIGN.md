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
- date : ...
- time : ...
- status : String