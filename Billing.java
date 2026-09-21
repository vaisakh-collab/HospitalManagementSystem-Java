import java.time.LocalDate;

public class Billing {

    private int billId;
    private Patient patient;
    private Consultation consultation;
    private double amount;
    private LocalDate date;
    private String status;


    // Constructor
    //CREATE A NEW BILLING OBJECT
    public Billing (Patient patient, Consultation consultation,
                double amount, LocalDate date) {
        this.patient = patient;
        this.consultation = consultation;
        this.amount = amount;
        this.date = date;
        this.status = "Pending";
    }

    //RECONSTRUCT AN EXISTING BILLING OBJECT FROM DATABASE
    public Billing (int billId,Patient patient, Consultation consultation,
                double amount, LocalDate date, String status) {

        this.billId = billId;

        this.patient = patient;
        this.consultation = consultation;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public void setBillId(int billId){
        this.billId = billId;
    }

    // Get Bill ID
    public int getBillId() {
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

    // Mark Bill as Paid
    public void markAsPaid() {
        this.status = "Paid";
    }
}