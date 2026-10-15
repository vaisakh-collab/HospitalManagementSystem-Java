import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginWindow extends JFrame {
    private static final Color NAVY = new Color(35, 45, 65);
    private static final Color BLUE = new Color(48, 112, 190);
    private static final Color BG = new Color(245, 248, 252);

    public LoginWindow() {
        setTitle("Hospital Management System");
        setSize(460, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel("HOSPITAL SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(NAVY);
        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("Select your login type", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 22, 0);
        panel.add(subtitle, gbc);

        JButton patientButton = createButton("Patient Login");
        JButton doctorButton = createButton("Doctor Login");
        JButton adminButton = createButton("Admin Login");
        JButton createAccountButton = createButton("Create Patient Account");
        createAccountButton.setBackground(new Color(65, 135, 105));

        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.gridy = 2; panel.add(patientButton, gbc);
        gbc.gridy = 3; panel.add(doctorButton, gbc);
        gbc.gridy = 4; panel.add(adminButton, gbc);
        gbc.gridy = 5; panel.add(createAccountButton, gbc);

        patientButton.addActionListener(e -> openLogin(UserRole.PATIENT));
        doctorButton.addActionListener(e -> openLogin(UserRole.DOCTOR));
        adminButton.addActionListener(e -> openLogin(UserRole.ADMIN));
        createAccountButton.addActionListener(e -> {
            new PatientRegistrationWindow().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 43));
        return button;
    }

    private void openLogin(UserRole role) {
        new RoleLoginWindow(role).setVisible(true);
        dispose();
    }
}
