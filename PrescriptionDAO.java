import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    private static final String BASE_SELECT =
        "SELECT pr.prescription_id, pr.medication, pr.dosage, pr.frequency, pr.duration, pr.instructions, " +
        "       p.patient_id, p.name AS p_name, p.age AS p_age, p.phone AS p_phone, p.address AS p_address, p.email AS p_email, " +
        "       d.doctor_id, d.name AS d_name, d.age AS d_age, d.phone AS d_phone, d.address AS d_address, d.email AS d_email, d.specialization AS d_specialization " +
        "FROM Prescription pr " +
        "LEFT JOIN Patient p ON pr.patient_id = p.patient_id " +
        "LEFT JOIN Doctor d ON pr.doctor_id = d.doctor_id ";

    public boolean addPrescription(Prescription prescription) {
        if (prescription == null) {
            return false;
        }

        String sql = "INSERT INTO Prescription (patient_id, doctor_id, medication, dosage, frequency, duration, instructions) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (prescription.getPatient() != null) {
                ps.setInt(1, prescription.getPatient().getPatientId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (prescription.getDoctor() != null) {
                ps.setInt(2, prescription.getDoctor().getDoctorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, prescription.getMedication());
            ps.setString(4, prescription.getDosage());
            ps.setString(5, prescription.getFrequency());
            ps.setString(6, prescription.getDuration());
            ps.setString(7, prescription.getInstructions());

            if (ps.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    prescription.setPrescriptionId(keys.getInt(1));
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Prescription findPrescription(int prescriptionId) {
        String sql = BASE_SELECT + "WHERE pr.prescription_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, prescriptionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public boolean updatePrescription(Prescription prescription) {
        if (prescription == null) {
            return false;
        }

        String sql = "UPDATE Prescription SET patient_id = ?, doctor_id = ?, medication = ?, dosage = ?, " +
                     "frequency = ?, duration = ?, instructions = ? WHERE prescription_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (prescription.getPatient() != null) {
                ps.setInt(1, prescription.getPatient().getPatientId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (prescription.getDoctor() != null) {
                ps.setInt(2, prescription.getDoctor().getDoctorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, prescription.getMedication());
            ps.setString(4, prescription.getDosage());
            ps.setString(5, prescription.getFrequency());
            ps.setString(6, prescription.getDuration());
            ps.setString(7, prescription.getInstructions());
            ps.setInt(8, prescription.getPrescriptionId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePrescription(int prescriptionId) {
        String sql = "DELETE FROM Prescription WHERE prescription_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, prescriptionId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescription> getPrescriptionsByPatient(int patientId) {
        String sql = BASE_SELECT + "WHERE pr.patient_id = ?";
        return queryList(sql, patientId);
    }

    public List<Prescription> getPrescriptionsByDoctor(int doctorId) {
        String sql = BASE_SELECT + "WHERE pr.doctor_id = ?";
        return queryList(sql, doctorId);
    }

    private List<Prescription> queryList(String sql, int paramId) {
        List<Prescription> prescriptions = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, paramId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prescriptions.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return prescriptions;
    }

    private Prescription map(ResultSet rs) throws SQLException {
        int patientId = rs.getInt("patient_id");
        Patient patient = null;
        if (!rs.wasNull() && patientId > 0) {
            patient = new Patient(
                patientId,
                rs.getString("p_name"),
                rs.getInt("p_age"),
                rs.getString("p_phone"),
                rs.getString("p_address"),
                rs.getString("p_email")
            );
        }

        int doctorId = rs.getInt("doctor_id");
        Doctor doctor = null;
        if (!rs.wasNull() && doctorId > 0) {
            doctor = new Doctor(
                doctorId,
                rs.getString("d_name"),
                rs.getInt("d_age"),
                rs.getString("d_phone"),
                rs.getString("d_address"),
                rs.getString("d_email"),
                rs.getString("d_specialization")
            );
        }

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
