import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConsultationDAO {

    public boolean addConsultation(Consultation consultation){

        String sql = "INSERT INTO Consultation " + 
             "(patient_id, doctor_id, consultation_date, consultation_time, diagnosis, notes, prescription_id) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?)";


        //try-catch and try-with-resources
        //try-with-resources automatically frees the resource after use is over
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
                statement.setInt(1, consultation.getPatient().getPatientId());
                statement.setInt(2, consultation.getDoctor().getDoctorId());
                statement.setDate(3, java.sql.Date.valueOf(consultation.getDate()));    //differentiate from java.util.Date, otherewise import java.util.Date
                statement.setTime(4, java.sql.Time.valueOf(consultation.getTime()));
                statement.setString(5, consultation.getDiagnosis());
                statement.setString(6, consultation.getNotes());
                if(consultation.getPrescription() != null){
                    statement.setInt(7,consultation.getPrescription().getPrescriptionId());
                } else{
                    statement.setNull(7, java.sql.Types.INTEGER);
                }
                
                int rowsAffected = statement.executeUpdate(); //sends commands to mySQL

                if(rowsAffected > 0){
                    ResultSet generatedKeys = statement.getGeneratedKeys();

                    if(generatedKeys.next()){
                        int generatedId = generatedKeys.getInt(1);
                        consultation.setConsultationId(generatedId);
                        return true;
                    }
                }

                return false;
        }
        catch(IOException e){
            System.out.println("Caught exception: "+ e.getMessage());
            return false;
        }
        catch(SQLException e){
            System.out.println("Caught excpetion: "+ e.getMessage());
            return false;
        }
    }

    public Consultation findConsultation(int consultation_id){
        /* 
        findConsultation(id) 
        → SQL SELECT using that ID 
        → retrieve the matching database row as ResultSet
        → construct and return a Consultation object.
        */
        String sql = "SELECT * FROM Consultation WHERE consultation_id = ?";


        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setInt(1,consultation_id);

                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()){

                    PatientDAO patientDAO_obj = new PatientDAO();
                    DoctorDAO doctorDAO_obj = new DoctorDAO();
                    PrescriptionDAO prescriptionDAO_obj = new PrescriptionDAO();
                    
                    //NULL CHECK FOR PRESCRIPTION
                    int prescriptionId = resultSet.getInt("prescription_id");
                    
                    Prescription prescription = null;

                    if(!resultSet.wasNull()){
                        prescription = prescriptionDAO_obj.findPrescription(prescriptionId);
                    }

                    Consultation consultation = new Consultation(
                        resultSet.getInt("consultation_id"),
                        patientDAO_obj.findPatient(resultSet.getInt("patient_id")), 
                        doctorDAO_obj.findDoctor(resultSet.getInt("doctor_id")), 
                        resultSet.getDate("consultation_date").toLocalDate(), 
                        resultSet.getTime("consultation_time").toLocalTime(), 
                        resultSet.getString("diagnosis"), 
                        resultSet.getString("notes"), 
                        prescription
                    );

                    return consultation;
                }

        }
        catch(IOException e){
            System.out.println("Caught exception: " + e.getMessage());
            return null;
            //no consultation object so return null pointer
        }
        catch(SQLException e){
            System.out.println("Caught exception: " + e.getMessage());
            return null;
            //no consultation object so return null pointer
        }

        return null;
    }
}
    public boolean updateConsultation(Consultation consultation){
        String sql = "UPDATE consultation"+
                     "SET patient_id = ?,"+
                     "doctor_id = ?,"+
                     "date = ?,"+ 
                     "time = ?,"+
                     "diagnosis = ?,"+
                     "notes = ?,"+
                     "presciption_id = ?"+
                     "WHERE consultation_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            //user will enter values in GUI and a consultation object will be created
            //Here we update the table using the values from that consultation object
            statement.setInt(1,consultation.getPatient().getPatientId());
            statement.setInt(2, consultation.getDoctor().getDoctorId());
            statement.setDate(3, java.sql.Date.valueOf(consultation.getDate()));
            //the mySQL date is different from the Java Date
            //Here in the Java model we have used LocalDate
            //But JDBC uses java.sql.Date
            //similarly below for Time
            statement.setTime(4, java.sql.Time.valueOf(consultation.getTime()));
            

        }
    }