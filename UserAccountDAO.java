
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class UserAccountDAO {

    public UserAccount findByUsername(String username)
            throws SQLException, IOException {

        String sql = """
            SELECT user_id, username, password_hash, role,
                   patient_id, doctor_id, is_active
            FROM UserAccount
            WHERE username = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                int patientValue = rs.getInt("patient_id");
                Integer patientId = rs.wasNull()
                        ? null : patientValue;

                int doctorValue = rs.getInt("doctor_id");
                Integer doctorId = rs.wasNull()
                        ? null : doctorValue;

                return new UserAccount(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    UserRole.valueOf(rs.getString("role")),
                    patientId,
                    doctorId,
                    rs.getBoolean("is_active")
                );
            }
        }
    }

    public boolean createAccount(
            String username,
            String password,
            UserRole role,
            Integer patientId,
            Integer doctorId)
            throws SQLException, IOException, Exception {

        if (username == null || username.isBlank()
                || password == null || password.isEmpty()
                || role == null) {
            return false;
        }

        if (role == UserRole.ADMIN) {
            if (patientId != null || doctorId != null) {
                return false;
            }
        } else if (role == UserRole.PATIENT) {
            if (patientId == null || doctorId != null) {
                return false;
            }
        } else if (role == UserRole.DOCTOR) {
            if (doctorId == null || patientId != null) {
                return false;
            }
        }

        String passwordHash = PasswordUtil.hashPassword(password.toCharArray());

        String sql = """
            INSERT INTO UserAccount
                (username, password_hash, role,
                 patient_id, doctor_id, is_active)
            VALUES (?, ?, ?, ?, ?, TRUE)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.setString(3, role.name());

            if (patientId == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, patientId);
            }

            if (doctorId == null) {
                statement.setNull(5, java.sql.Types.INTEGER);
            } else {
                statement.setInt(5, doctorId);
            }

            return statement.executeUpdate() == 1;
        }
    }
}
