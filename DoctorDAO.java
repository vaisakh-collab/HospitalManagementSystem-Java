import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean addDoctor(Doctor doctor) {
        if (doctor == null) return false;
        String sql = "INSERT INTO Doctor (name, age, phone, address, email, specialization) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, doctor.getName());
            ps.setInt(2, doctor.getAge());
            ps.setString(3, doctor.getPhone());
            ps.setString(4, doctor.getAddress());
            ps.setString(5, doctor.getEmail());
            ps.setString(6, doctor.getSpecialization());

            if (ps.executeUpdate() == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    doctor.setDoctorId(keys.getInt(1));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Doctor findDoctor(int doctorId) {
        String sql = "SELECT * FROM Doctor WHERE doctor_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
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

    public boolean updateDoctor(Doctor doctor) {
        if (doctor == null) return false;
        String sql = "UPDATE Doctor SET name = ?, age = ?, phone = ?, address = ?, email = ?, specialization = ? WHERE doctor_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, doctor.getName());
            ps.setInt(2, doctor.getAge());
            ps.setString(3, doctor.getPhone());
            ps.setString(4, doctor.getAddress());
            ps.setString(5, doctor.getEmail());
            ps.setString(6, doctor.getSpecialization());
            ps.setInt(7, doctor.getDoctorId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM Doctor WHERE doctor_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Required by DAO_DETAILS.md contract
    public List<Appointment> getAppointments(int doctorId) {
        return new AppointmentDAO().getAppointmentsByDoctor(doctorId);
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return list;
    }

    private Doctor map(ResultSet rs) throws SQLException {
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
