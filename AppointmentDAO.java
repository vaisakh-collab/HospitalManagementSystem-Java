import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String BASE_SELECT =
        "SELECT a.appointment_id, a.date, a.time, a.status, " +
        "       p.patient_id, p.name AS p_name, p.age AS p_age, p.phone AS p_phone, p.address AS p_address, p.email AS p_email, " +
        "       d.doctor_id, d.name AS d_name, d.age AS d_age, d.phone AS d_phone, d.address AS d_address, d.email AS d_email, d.specialization AS d_specialization " +
        "FROM Appointment a " +
        "LEFT JOIN Patient p ON a.patient_id = p.patient_id " +
        "LEFT JOIN Doctor d ON a.doctor_id = d.doctor_id ";

    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) return false;
        String sql = "INSERT INTO Appointment (patient_id, doctor_id, date, time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (appointment.getPatient() != null) {
                ps.setInt(1, appointment.getPatient().getPatientId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (appointment.getDoctor() != null) {
                ps.setInt(2, appointment.getDoctor().getDoctorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (appointment.getDate() != null) {
                ps.setDate(3, Date.valueOf(appointment.getDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            if (appointment.getTime() != null) {
                ps.setTime(4, Time.valueOf(appointment.getTime()));
            } else {
                ps.setNull(4, Types.TIME);
            }

            ps.setString(5, appointment.getStatus());

            if (ps.executeUpdate() == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    appointment.setAppointmentId(keys.getInt(1));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Appointment findAppointment(int appointmentId) {
        String sql = BASE_SELECT + "WHERE a.appointment_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
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

    public boolean updateAppointment(Appointment appointment) {
        if (appointment == null) return false;
        String sql = "UPDATE Appointment SET patient_id = ?, doctor_id = ?, date = ?, time = ?, status = ? WHERE appointment_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (appointment.getPatient() != null) {
                ps.setInt(1, appointment.getPatient().getPatientId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (appointment.getDoctor() != null) {
                ps.setInt(2, appointment.getDoctor().getDoctorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (appointment.getDate() != null) {
                ps.setDate(3, Date.valueOf(appointment.getDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            if (appointment.getTime() != null) {
                ps.setTime(4, Time.valueOf(appointment.getTime()));
            } else {
                ps.setNull(4, Types.TIME);
            }

            ps.setString(5, appointment.getStatus());
            ps.setInt(6, appointment.getAppointmentId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointment WHERE appointment_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        String sql = BASE_SELECT + "WHERE a.patient_id = ?";
        return queryList(sql, patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        String sql = BASE_SELECT + "WHERE a.doctor_id = ?";
        return queryList(sql, doctorId);
    }

    private List<Appointment> queryList(String sql, int paramId) {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, paramId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(map(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return appointments;
    }

    private Appointment map(ResultSet rs) throws SQLException {
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

        Date sqlDate = rs.getDate("date");
        LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        Time sqlTime = rs.getTime("time");
        LocalTime time = (sqlTime != null) ? sqlTime.toLocalTime() : null;

        return new Appointment(
            rs.getInt("appointment_id"),
            patient,
            doctor,
            date,
            time,
            rs.getString("status")
        );
    }
}
