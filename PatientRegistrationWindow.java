import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PatientRegistrationWindow extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    public PatientRegistrationWindow() {
        setTitle("Create Patient Account");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 248, 252));
        form.setBorder(new EmptyBorder(18, 35, 18, 35));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 0, 4, 0);

        JLabel title = new JLabel("CREATE PATIENT ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 21));
        title.setForeground(new Color(35, 45, 65));
        g.gridy = 0; g.insets = new Insets(0, 0, 12, 0); form.add(title, g);
        g.insets = new Insets(4, 0, 3, 0);

        addField(form, g, 1, "Full name", nameField);
        addField(form, g, 3, "Age", ageField);
        addField(form, g, 5, "Phone", phoneField);
        addField(form, g, 7, "Address", addressField);
        addField(form, g, 9, "Email", emailField);
        addField(form, g, 11, "Username", usernameField);
        addField(form, g, 13, "Password", passwordField);
        addField(form, g, 15, "Confirm password", confirmPasswordField);

        JButton createButton = new JButton("Create Account");
        JButton backButton = new JButton("Back to Login");
        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setOpaque(false);
        buttons.add(backButton); buttons.add(createButton);
        g.gridy = 17; g.insets = new Insets(15, 0, 0, 0); form.add(buttons, g);

        createButton.addActionListener(e -> createAccount());
        confirmPasswordField.addActionListener(e -> createAccount());
        backButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });
        setContentPane(new JScrollPane(form));
    }

    private void addField(JPanel panel, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy = row;
        panel.add(new JLabel(label), g);
        g.gridy = row + 1;
        field.setPreferredSize(new Dimension(300, 30));
        panel.add(field, g);
    }

    private void createAccount() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirm = confirmPasswordField.getPassword();

        try {
            if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()
                    || address.isEmpty() || email.isEmpty() || username.isEmpty()
                    || password.length == 0 || confirm.length == 0) {
                showError("Please fill in every field.");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age < 1 || age > 120) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError("Enter a valid age between 1 and 120.");
                return;
            }
            if (!java.util.Arrays.equals(password, confirm)) {
                showError("The passwords do not match.");
                return;
            }
            if (password.length < 8) {
                showError("Use a password with at least 8 characters.");
                return;
            }

            boolean created = registerPatient(name, age, phone, address, email, username, password);
            if (created) {
                JOptionPane.showMessageDialog(this,
                    "Patient account created successfully. You can now log in.",
                    "Account Created", JOptionPane.INFORMATION_MESSAGE);
                new RoleLoginWindow(UserRole.PATIENT).setVisible(true);
                dispose();
            } else {
                showError("Could not create the account. The username may already be in use.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Could not create the account. Check the database connection and configuration.");
        } finally {
            java.util.Arrays.fill(password, '\0');
            java.util.Arrays.fill(confirm, '\0');
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    /*
     * Inserts Patient and UserAccount in one transaction.
     * If either insert fails, both changes are rolled back.
     */
    private boolean registerPatient(String name, int age, String phone, String address,
                                    String email, String username, char[] password) throws Exception {
        String patientSql = "INSERT INTO Patient (name, age, phone, address, email) VALUES (?, ?, ?, ?, ?)";
        String accountSql = "INSERT INTO UserAccount (username, password_hash, role, patient_id, doctor_id, is_active) "
                          + "VALUES (?, ?, 'PATIENT', ?, NULL, TRUE)";

        String passwordHash = PasswordUtil.hashPassword(password);

        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int patientId;
                try (PreparedStatement ps = c.prepareStatement(patientSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name);
                    ps.setInt(2, age);
                    ps.setString(3, phone);
                    ps.setString(4, address);
                    ps.setString(5, email);
                    if (ps.executeUpdate() != 1) throw new SQLException("Patient insert failed.");
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Could not retrieve patient ID.");
                        patientId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(accountSql)) {
                    ps.setString(1, username);
                    ps.setString(2, passwordHash);
                    ps.setInt(3, patientId);
                    ps.executeUpdate();
                }

                c.commit();
                return true;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }
}
