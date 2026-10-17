
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

}
  
