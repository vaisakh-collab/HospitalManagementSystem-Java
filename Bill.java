import java.time.LocalDate;

public class Bill {

    private String billId;
    private Patient patient;
    private Consultation consultation;
    private double amount;
    private LocalDate date;
    private String status;

    // Used to generate unique Bill IDs
    private static int nextId = 1;

    // Constructor
    public Bill(Patient patient, Consultation consultation,
                double amount, LocalDate date, String status) {

        this.billId = String.format("B%03d", nextId++);

        this.patient = patient;
        this.consultation = consultation;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Get Bill ID
    public String getBillId() {
        return billId;
    }

    // Get Patient
    public Patient getPatient() {
        return patient;
    }

    // Get Consultation
    public Consultation getConsultation() {
        return consultation;
    }

    // Get Amount
    public double getAmount() {
        return amount;
    }

    // Get Date
    public LocalDate getDate() {
        return date;
    }

    // Get Status
    public String getStatus() {
        return status;
    }

    // Set Status
    public void setStatus(String status) {
        this.status = status;
    }

    // Mark Bill as Paid
    public void markAsPaid() {
        this.status = "Paid";
    }
}