import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Doctor extends Person {

    private int doctorId;
    private String specialization;

    //CREATE A NEW DOCTOR
    public Doctor(String name, int age, String phone, String address,
                  String email, String specialization) {

        super(name, age, phone, address, email);
        this.specialization = specialization;
    }
    //RECONSTRUCTING AN EXISTING DOCTOR FROM DATABASE
    public Doctor(int doctorId, String name, int age, String phone, String address,
                  String email, String specialization) {

        super(name, age, phone, address, email);

        this.doctorId = doctorId;
        this.specialization = specialization;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return this.doctorId;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<Appointment> viewAppointments() {
        return null;
    }

    public boolean isAvailable(LocalDate requestedDate, LocalTime requestedTime) {
        return true;
    }
}