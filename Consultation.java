import java.time.LocalDate;
import java.time.LocalTime;

public class Consultation {

    private int consultationId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private String diagnosis;
    private String notes;
    private Prescription prescription;

    //CREATE A NEW CONSULTATION
    public Consultation(Patient patient, Doctor doctor,
                        LocalDate date, LocalTime time,
                        String diagnosis, String notes,
                        Prescription prescription) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.prescription = prescription;
    }

    //RECONSTRUCTING AN EXISTING CONSULTATION FROM DATABASE
    public Consultation(int consultationId, Patient patient, Doctor doctor,
                        LocalDate date, LocalTime time,
                        String diagnosis, String notes,
                        Prescription prescription) {
                            
        this.consultationId = consultationId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.prescription = prescription;
    }

    public void setConsultationId(int consultationId){
        this.consultationId = consultationId;
    }

    public int getConsultationId() {
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

}
