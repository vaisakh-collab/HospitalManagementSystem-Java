import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConsultationDAO {

    String sql = "INSERT INTO Consultation " + 
                 "(consultation_id, patient_id, doctor_id, consultation_date, consultation_time, diagnosis, notes, prescription_id) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public boolean addConsultation(Consultation consultation){
        //try-catch and try-with-resources
        //try-with-resources automatically frees the resource after use is over
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, consultation.getConsultationId());
                statement.setString(2, consultation.getPatient().getPatientId());
                statement.setString(3, consultation.getDoctor().getDoctorId());
                statement.setDate(4, java.sql.Date.valueOf(consultation.getDate()));    //differentiate from java.util.Date, otherewise import java.util.Date
                statement.setTime(5, java.sql.Time.valueOf(consultation.getTime()));
                statement.setString(6, consultation.getDiagnosis());
                statement.setString(7, consultation.getNotes());
                if(consultation.getPrescription() != null){
                    statement.setString(8,consultation.getPrescription().getPrescriptionId());
                } else{
                    statement.setNull(8, java.sql.Types.VARCHAR);
                }
                
                int rowsAffected = statement.executeUpdate(); //sends commands to mySQL

                return rowsAffected > 0;
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

    public Consultation findConsultation(String consultation_id){
        /* 
        findConsultation(id) 
        → SQL SELECT using that ID 
        → retrieve the matching database row as ResultSet
        → construct and return a Consultation object.
        */
        String sql = "SELECT * FROM Consultation WHERE consultation_id = ?";


        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setString(1,consultation_id);

                java.sql.ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()){

                    PatientDAO patientDAO_obj = new PatientDAO();
                    DoctorDAO doctorDAO_obj = new DoctorDAO();

                    Consultation consultation = new Consultation(
                        patientDAO_obj.findPatient(resultSet.getString("patient_id")), 
                        doctorDAO_obj.findDoctor(resultSet.getString("doctor_id")), 
                        resultSet.getDate("consultation_date").toLocalDate(), 
                        resultSet.getTime("consultation_time").toLocalTime(), 
                        resultSet.getString("diagnosis"), 
                        resultSet.getString("notes"), 
                        PrescriptionDAO.getPrescription(resultSet.getString("prescription_id"))
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
    }
}
