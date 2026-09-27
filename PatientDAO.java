import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public boolean addPatient(Patient patient) {

        String sql = "INSERT INTO Patient (name, age, phone, address, email) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement+-(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getPhone());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getEmail());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    patient.setPatientId(keys.getInt(1));
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Patient findPatient(int patientId) {

        String sql = "SELECT * FROM Patient WHERE patient_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean updatePatient(Patient patient) {

        String sql = "UPDATE Patient SET "
                   + "name = ?, age = ?, phone = ?, address = ?, email = ? "
                   + "WHERE patient_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getPhone());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getEmail());
            ps.setInt(6, patient.getPatientId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


     public boolean deletePatient(int patientId) {

        String sql = "DELETE FROM Patient WHERE patient_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Get appointments of a patient
    public List<Appointment> getAppointments(int patientId) {

        return new AppointmentDAO().getAppointmentsByPatient(patientId);
    }


    // Get medical history of a patient
    public List<Consultation> getMedicalHistory(int patientId) {

        List<Consultation> consultations = new ArrayList<>();

        String sql = "SELECT * FROM Consultation "
                   + "WHERE patient_id = ? "
                   + "ORDER BY consultation_date DESC, consultation_time DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Patient patient = findPatient(
                            rs.getInt("patient_id")
                    );

                    Doctor doctor = findDoctor(
                            rs.getInt("doctor_id")
                    );

                    Prescription prescription = null;

                    int prescriptionId =
                            rs.getInt("prescription_id");

                    if (!rs.wasNull()) {
                        prescription =
                                findPrescription(prescriptionId);
                    }

                    Consultation consultation =
                            new Consultation(
                                    rs.getInt("consultation_id"),
                                    patient,
                                    doctor,
                                    rs.getDate("consultation_date")
                                            .toLocalDate(),
                                    rs.getTime("consultation_time")
                                            .toLocalTime(),
                                    rs.getString("diagnosis"),
                                    rs.getString("notes"),
                                    prescription
                            );

                    consultations.add(consultation);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }


    // Get prescriptions of a patient
    public List<Prescription> getPrescriptions(int patientId) {

        List<Prescription> prescriptions = new ArrayList<>();

        String sql = "SELECT * FROM Prescription "
                   + "WHERE patient_id = ? "
                   + "ORDER BY prescription_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Patient patient = findPatient(
                            rs.getInt("patient_id")
                    );

                    Doctor doctor = findDoctor(
                            rs.getInt("doctor_id")
                    );

                    Prescription prescription =
                            new Prescription(
                                    rs.getInt("prescription_id"),
                                    patient,
                                    doctor,
                                    rs.getString("medication"),
                                    rs.getString("dosage"),
                                    rs.getString("frequency"),
                                    rs.getString("duration"),
                                    rs.getString("instructions")
                            );

                    prescriptions.add(prescription);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prescriptions;
    }


    private Doctor findDoctor(int doctorId) {

        String sql = "SELECT * FROM Doctor WHERE doctor_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Doctor(
                            rs.getInt("doctor_id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getString("specialization")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Prescription findPrescription(int prescriptionId) {

        String sql = "SELECT * FROM Prescription "
                   + "WHERE prescription_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, prescriptionId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Patient patient =
                            findPatient(rs.getInt("patient_id"));

                    Doctor doctor =
                            findDoctor(rs.getInt("doctor_id"));

                    return new Prescription(
                            rs.getInt("prescription_id"),
                            patient,
                            doctor,
                            rs.getString("medication"),
                            rs.getString("dosage"),
                            rs.getString("frequency"),
                            rs.getString("duration"),
                            rs.getString("instructions")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Patient map(ResultSet rs) throws SQLException {

        return new Patient(
                rs.getInt("patient_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("email")
        );
    }
}