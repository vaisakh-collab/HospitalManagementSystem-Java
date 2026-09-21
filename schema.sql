CREATE DATABASE hospital;

USE hospital;

CREATE TABLE Patient (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    age INT,
    phone VARCHAR(100),
    address VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE Doctor (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    age INT,
    phone VARCHAR(100),
    address VARCHAR(100),
    email VARCHAR(100),
    specialization VARCHAR(20)
);

CREATE TABLE Prescription (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    doctor_id INT,
    medication VARCHAR(100),
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    duration VARCHAR(100),
    instructions VARCHAR(255),

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
);

CREATE TABLE Appointment (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    doctor_id INT,
    date DATE,
    time TIME,
    status VARCHAR(50),

    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
);


CREATE TABLE Consultation (
    consultation_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    consultation_date DATE NOT NULL,
    consultation_time TIME NOT NULL,
    diagnosis TEXT,
    notes TEXT,
    prescription_id INT,
    UNIQUE(prescription_id),

    CONSTRAINT fk_consultation_patient
        FOREIGN KEY (patient_id)
        REFERENCES patient(patient_id),

    CONSTRAINT fk_consultation_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctor(doctor_id),

    CONSTRAINT fk_consultation_prescription
        FOREIGN KEY (prescription_id)
        REFERENCES prescription(prescription_id)
);
CREATE TABLE Billing (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    consultation_id INT,
    amount DECIMAL(10,2),
    date DATE,
    status VARCHAR(50),

    CONSTRAINT fk_billing_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_billing_consultation
        FOREIGN KEY (consultation_id)
        REFERENCES Consultation(consultation_id)
);
