import java.util.List;
public class Patient extends Person {
  private int patientId;

  //CREATE A NEW PATIENT
  public Patient(String name, int age, String phone, String address, String email){
    super(name, age, phone, address, email);
  }

  //RECONSTRUCTING AN EXISTING PATIENT FROM DATABASE
  public Patient(int patientId, String name, int age, String phone, String address, String email){
    super(name, age, phone, address, email);
    this.patientId = patientId;
  }

  public void setPatientId(int patientId){
    this.patientId = patientId;
  }

  public int getPatientId(){
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
  
