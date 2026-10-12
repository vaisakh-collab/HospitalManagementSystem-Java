
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class LoginWindow extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthenticationService authenticationService;

    public LoginWindow() {

        authenticationService = new AuthenticationService();

        setTitle("Hospital Management System - Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 248, 252));
        mainPanel.setBorder(
            new EmptyBorder(30, 40, 30, 40)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel titleLabel = new JLabel(
            "HOSPITAL SYSTEM", SwingConstants.CENTER
        );
        titleLabel.setFont(
            new Font("SansSerif", Font.BOLD, 25)
        );
        titleLabel.setForeground(new Color(35, 45, 65));

        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel(
            "Please log in to continue",
            SwingConstants.CENTER
        );
        subtitleLabel.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(8, 0, 5, 0);
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Username"), gbc);

        usernameField = new JTextField();
        usernameField.setPreferredSize(
            new Dimension(300, 38)
        );

        gbc.gridy = 3;
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 4;
        mainPanel.add(new JLabel("Password"), gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(
            new Dimension(300, 38)
        );

        gbc.gridy = 5;
        mainPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Log In");
        loginButton.setFont(
            new Font("SansSerif", Font.BOLD, 15)
        );
        loginButton.setBackground(new Color(48, 112, 190));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(
            new Dimension(300, 42)
        );

        gbc.gridy = 6;
        gbc.insets = new Insets(25, 0, 10, 0);
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());

        add(mainPanel);
    }

    private void login() {

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            java.util.Arrays.fill(password, '\0');

            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            UserAccount account =
                authenticationService.login(username, password);

            if (account == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password, or account inactive.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                passwordField.setText("");
                return;
            }

            // Temporary integration: opens the existing main window.
            // Role-based access restrictions must also be implemented.
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();

        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to connect to the authentication database.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginWindow window = new LoginWindow();
            window.setVisible(true);
        });
    }
}
