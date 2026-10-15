import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class RoleLoginWindow extends JFrame {
    private final UserRole expectedRole;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthenticationService authenticationService;

    public RoleLoginWindow(UserRole expectedRole) {
        this.expectedRole = expectedRole;
        this.authenticationService = new AuthenticationService();

        setTitle(expectedRole.name() + " Login");
        setSize(440, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 248, 252));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel(expectedRole.name() + " LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(35, 45, 65));
        gbc.gridy = 0; gbc.insets = new Insets(5, 0, 25, 0); panel.add(title, gbc);

        gbc.insets = new Insets(8, 0, 5, 0);
        gbc.gridy = 1; panel.add(new JLabel("Username"), gbc);
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 2; panel.add(usernameField, gbc);

        gbc.gridy = 3; panel.add(new JLabel("Password"), gbc);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 4; panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Log In");
        JButton backButton = new JButton("Back");
        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setOpaque(false); buttons.add(backButton); buttons.add(loginButton);
        gbc.gridy = 5; gbc.insets = new Insets(20, 0, 5, 0); panel.add(buttons, gbc);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        backButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });
        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        if (username.isEmpty() || password.length == 0) {
            java.util.Arrays.fill(password, '\0');
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UserAccount account = authenticationService.login(username, password, expectedRole);
            if (account == null) {
                JOptionPane.showMessageDialog(this,
                    "Invalid credentials or account not permitted for this login type.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                return;
            }

            if (account.getRole() == UserRole.PATIENT) {
                if (account.getPatientId() == null) {
                    JOptionPane.showMessageDialog(this, "This patient account is not linked to a patient record.",
                        "Account Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new PatientDashboard(account).setVisible(true);
            } else {
                // Doctor/admin dashboards have not been implemented yet.
                new MainWindow().setVisible(true);
            }
            dispose();
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to the database.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An authentication error occurred.",
                "Authentication Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
