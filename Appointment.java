public class Appointment{
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private String status; //"Scheduled", "Completed", "Cancelled"

    private static int nextId = 1; //keep track of AppointmentId to create an id each time constructor is called

    //constructor

    public Appointment(Patient patient, Doctor doctor, LocalDate date, LocalTime time){

        //this.appointmentId = "A" + nextId++;       // create appointmentId as A1, A2, ...

        this.appointmentId = String.format("A%03d", nextId++)     // padding zeroes till 3 digits as A001, A002, ...

        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = "Scheduled";
    }

    //getter methods

    public String getAppointmentId(){
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