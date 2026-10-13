
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class AuthenticationService {

    private final UserAccountDAO userAccountDAO;

    public AuthenticationService() {
        userAccountDAO = new UserAccountDAO();
    }

    public UserAccount login(String username, char[] password)
            throws SQLException, IOException, Exception {

        if (username == null || username.isBlank()
                || password == null || password.length == 0) {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
            return null;
        }

        try {
            UserAccount account =
                userAccountDAO.findByUsername(username.trim());

            if (account == null || !account.isActive()) {
                return null;
            }

            boolean valid = PasswordUtil.verifyPassword(
                password, account.getPasswordHash()
            );

            return valid ? account : null;

        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
