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