import java.util.ArrayList;
import java.util.List;

public class HospitalManagementSystem {

    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Appointment> appointments;
    private List<Consultation> consultations;
    private List<Billing> bills;
    private List<Prescription> prescriptions;

    public HospitalManagementSystem() {
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        consultations = new ArrayList<>();
        bills = new ArrayList<>();
        prescriptions = new ArrayList<>();
    }

    public boolean addPatient(Patient patient) {
        return patients.add(patient);
    }

    public boolean addDoctor(Doctor doctor) {
        return doctors.add(doctor);
    }

    public boolean bookAppointment(Appointment appointment) {
        return appointments.add(appointment);
    }

    public boolean cancelAppointment(int appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() == appointmentId) {
                appointment.cancelAppointment();
                return true;
            }
        }
        return false;
    }

    public boolean createConsultation(Consultation consultation) {
        return consultations.add(consultation);
    }

    public boolean createPrescription(Prescription prescription) {
        return prescriptions.add(prescription);
    }

    public boolean generateBill(Billing bill) {
        return bills.add(bill);
    }

    public Patient findPatient(int patientId) {
        for (Patient patient : patients) {
            if (patient.getPatientId() == patientId) {
                return patient;
            }
        }
        return null;
    }

    public Doctor findDoctor(int doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId() == doctorId) {
                return doctor;
            }
        }
        return null;
    }
}