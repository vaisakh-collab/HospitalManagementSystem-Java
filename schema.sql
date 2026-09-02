CREATE DATABASE hospital;

USE hospital;

CREATE TABLE Patient (
    patient_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    phone VARCHAR(100),
    address VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE Prescription (
    prescription_id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(50),
    doctor_id VARCHAR(50),
    medication VARCHAR(100),
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    duration VARCHAR(100),
    instructions VARCHAR(255),

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
);

CREATE TABLE Appointment (
    appointment_id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(50),
    doctor_id VARCHAR(50),
    date DATE,
    time TIME,
    status VARCHAR(50),

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
);

CREATE TABLE Billing (
    bill_id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(50),
    consultation_id VARCHAR(50),
    amount DECIMAL(10,2),
    date DATE,
    time TIME,
    status VARCHAR(50),

    CONSTRAINT fk_billing_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_billing_consultation
        FOREIGN KEY (consultation_id)
        REFERENCES Consultation(consultation_id)
);