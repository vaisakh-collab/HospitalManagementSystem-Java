-- Consultation table generated from Consultation.java
-- Assumes Patient, Doctor, and Prescription tables already exist.

CREATE TABLE consultation (
    consultation_id VARCHAR(20) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    consultation_date DATE NOT NULL,
    consultation_time TIME NOT NULL,
    diagnosis TEXT,
    notes TEXT,
    prescription_id VARCHAR(20),

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

-- Example record
INSERT INTO consultation (
    consultation_id,
    patient_id,
    doctor_id,
    consultation_date,
    consultation_time,
    diagnosis,
    notes,
    prescription_id
) VALUES (
    'CON1',
    'PAT1',
    'DOC1',
    '2026-09-02',
    '10:30:00',
    'Example diagnosis',
    'Example consultation notes',
    'PRE1'
);
