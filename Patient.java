import java.util.List;
public class Patient extends Person {
  private String patientId;
  private static int nextId = 1;
  public Patient(String name, int age, String phone, String address, String email){
    super(name, age, phone, address, email);
    this.patientId = "P" + nextId;
    nextId++;
  }

  public String getPatientId(){
    return this.patientId;
  }

  public List<Consultation> viewMedicalHistory(){
    return null;
  }

  public List<Appointment> viewAppointments(){
    return null;
  }

  public List<Prescription> viewPrescriptions(){
    return null;
  }
}
  
