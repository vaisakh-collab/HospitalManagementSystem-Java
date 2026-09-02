import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Doctor extends Person {

    private String doctorId;
    private String specialization;
    private static int nextId = 1;

    public Doctor(String name, int age, String phone, String address,
                  String email, String specialization) {

        super(name, age, phone, address, email);

        this.doctorId = String.format("D%03d", nextId++);
        this.specialization = specialization;
    }

    public String getDoctorId() {
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