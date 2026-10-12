
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {

        setTitle("Hospital Management System - Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 248, 252));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Heading
        JLabel titleLabel = new JLabel("HOSPITAL SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        titleLabel.setForeground(new Color(35, 45, 65));

        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel(
            "Please log in to continue",
            SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subtitleLabel, gbc);

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridy = 2;
        gbc.insets = new Insets(8, 0, 5, 0);
        mainPanel.add(usernameLabel, gbc);

        // Username field
        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        usernameField.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 3;
        mainPanel.add(usernameField, gbc);

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridy = 4;
        mainPanel.add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 5;
        mainPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginButton.setBackground(new Color(48, 112, 190));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 42));

        gbc.gridy = 6;
        gbc.insets = new Insets(25, 0, 10, 0);
        mainPanel.add(loginButton, gbc);

        // Login action
        loginButton.addActionListener(e -> login());

        // Pressing Enter in the password field also logs in
        passwordField.addActionListener(e -> login());

        add(mainPanel);
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Temporary login validation.
        // Replace this with actual authentication later.
        if (username.equals("admin") && password.equals("admin123")) {

            JOptionPane.showMessageDialog(
                this,
                "Login successful!"
            );

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
