
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class AuthenticationService {

    private final UserAccountDAO userAccountDAO;

    public AuthenticationService() {
        userAccountDAO = new UserAccountDAO();
    }

    public UserAccount login(
            String username,
            char[] password,
            UserRole expectedRole)
            throws SQLException, IOException, Exception {

        if (password == null) {
            return null;
        }

        try {
            if (username == null || username.isBlank()
                    || password.length == 0
                    || expectedRole == null) {
                return null;
            }

            UserAccount account =
                userAccountDAO.findByUsername(username.trim());

            if (account == null || !account.isActive()) {
                return null;
            }

            // The selected login page determines the permitted role.
            if (account.getRole() != expectedRole) {
                return null;
            }

            boolean passwordMatches =
                PasswordUtil.verifyPassword(
                    password,
                    account.getPasswordHash()
                );

            return passwordMatches ? account : null;

        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
