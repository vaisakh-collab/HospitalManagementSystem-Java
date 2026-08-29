public classs Prescription{
  private String prescriptionid;
  private Pateient patient;
  private Doctor doctor;
  private String medication;
  private String dosage;
  private String frequency;
  private String duration;
  private String instructions;
  private static int nexid=1;

  public prescription(Patient patient, Doctor doctor; String medication, String dosage, String frequency, String duration, String instructions){
    this.prescriptionid="P"+nextid++;
    this.patient=patient;
    this.doctor= doctor;
    this.medication = medication;
    this.dosage = dosage;
    this.frequency = frequency;
    this.duration = duration;
    this.instructions = instructions;
 public String getPrescriptionId() {
        return prescriptionId;
   }
 public Patient getPatient() {
        return patient;
    }
 public Doctor getDoctor() {
        return doctor;
    }
public String getMedication() {
        return medication;
    }
public String getDosage() {
        return dosage;
    }
public String getFrequency() {
        return frequency;
    }

public String getDuration() {
        return duration;
    }
public String getInstructions() {
        return instructions;
    }
public void setMedication(String medication) {
        this.medication = medication;
    }
public void setDosage(String dosage) {
        this.dosage = dosage;
    }
public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
