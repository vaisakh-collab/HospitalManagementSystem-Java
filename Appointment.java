import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment{
    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private String status; //"Scheduled", "Completed", "Cancelled"

    /* DISCARDED
    private static int nextId = 1; //keep track of AppointmentId to create an id each time constructor is called
    */
    //constructor

    //CREATING A NEW APPOINTMENT
    public Appointment(Patient patient, Doctor doctor, LocalDate date, LocalTime time){

        /*
         DISCARDED APPROACH

        //this.appointmentId = "A" + nextId++;       // create appointmentId as A1, A2, ...

        this.appointmentId = String.format("A%03d", nextId++);     // padding zeroes till 3 digits as A001, A002, ...

        */

        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = "Scheduled";
    }

    //RECONSTRUCTING AN EXISTING APPOINTMENT FROM DATABASE
    public Appointment(int appointmentId,Patient patient, Doctor doctor, LocalDate date, LocalTime time, String status){
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    //setter
    public void setAppointmentId(int appointmentId){
        this.appointmentId = appointmentId;
    }

    //getter methods

    public int getAppointmentId(){
        return this.appointmentId;
    }

    public Patient getPatient(){
        return this.patient;
    }

    public Doctor getDoctor(){
        return this.doctor;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public LocalTime getTime(){
        return this.time;
    }

    public String getStatus(){
        return this.status;
    }

    // Appointment methods

    public void cancelAppointment(){
        this.status = "Cancelled";
    }

    public void rescheduleAppointment(LocalDate date, LocalTime time){
        this.date = date;
        this.time = time;
    }

    public void completeAppointment(){
        this.status = "Completed";
    }
    
}