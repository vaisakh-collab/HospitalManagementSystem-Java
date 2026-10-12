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


CREATE TABLE UserAccount (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    patient_id INT UNIQUE,
    doctor_id INT UNIQUE,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_user_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id),

    CONSTRAINT fk_user_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id),

    CONSTRAINT chk_user_role
        CHECK (role IN ('ADMIN', 'DOCTOR', 'PATIENT')),

    CONSTRAINT chk_user_link
        CHECK (
            (role = 'ADMIN'
                AND patient_id IS NULL
                AND doctor_id IS NULL)
            OR
            (role = 'PATIENT'
                AND patient_id IS NOT NULL
                AND doctor_id IS NULL)
            OR
            (role = 'DOCTOR'
                AND doctor_id IS NOT NULL
                AND patient_id IS NULL)
        )
);
