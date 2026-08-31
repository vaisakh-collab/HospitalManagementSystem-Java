import java.time.LocalDate;
import java.time.LocalTime;

public class Consultation {

    private String consultationId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private String diagnosis;
    private String notes;
    private Prescription prescription;

    private static int nextId = 1;

    public Consultation(Patient patient, Doctor doctor,
                        LocalDate date, LocalTime time,
                        String diagnosis, String notes,
                        Prescription prescription) {

        this.consultationId = "CON" + nextId++;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.prescription = prescription;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public void displayConsultation() {

        System.out.println("\n===== Consultation Details =====");

        System.out.println("Consultation ID : " + consultationId);
        System.out.println("Patient         : " + patient);
        System.out.println("Doctor          : " + doctor);
        System.out.println("Date            : " + date);
        System.out.println("Time            : " + time);
        System.out.println("Diagnosis       : " + diagnosis);
        System.out.println("Notes           : " + notes);
        System.out.println("Prescription    : " + prescription);
    }
}
