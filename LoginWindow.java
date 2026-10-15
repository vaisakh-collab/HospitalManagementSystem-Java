import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class LoginWindow extends JFrame {

    private static final Color NAVY = new Color(35, 45, 65);
    private static final Color BLUE = new Color(48, 112, 190);
    private static final Color GREEN = new Color(40, 125, 90);
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

        JLabel title = new JLabel(
            "HOSPITAL SYSTEM", SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(NAVY);

        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel(
            "Select your login type", SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 22, 0);
        panel.add(subtitle, gbc);

        JButton patientButton = createButton(
            "Patient Login", BLUE
        );
        JButton doctorButton = createButton(
            "Doctor Login", BLUE
        );
        JButton adminButton = createButton(
            "Admin Login", BLUE
        );
        JButton createAccountButton = createButton(
            "Create Patient Account", GREEN
        );

        gbc.insets = new Insets(7, 0, 7, 0);

        gbc.gridy = 2;
        panel.add(patientButton, gbc);

        gbc.gridy = 3;
        panel.add(doctorButton, gbc);

        gbc.gridy = 4;
        panel.add(adminButton, gbc);

        gbc.gridy = 5;
        panel.add(createAccountButton, gbc);

        patientButton.addActionListener(e ->
            openLogin(UserRole.PATIENT)
        );

        doctorButton.addActionListener(e ->
            openLogin(UserRole.DOCTOR)
        );

        adminButton.addActionListener(e ->
            openLogin(UserRole.ADMIN)
        );

        createAccountButton.addActionListener(e -> {
            new PatientRegistrationWindow().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private JButton createButton(String text, Color background) {
        JButton button = new JButton(text);

        // Use Swing's basic button UI so custom colors
        // work consistently across operating systems.
        button.setUI(new BasicButtonUI());

        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(background);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 1),
                new EmptyBorder(10, 12, 10, 12)
            )
        );

        button.setPreferredSize(new Dimension(300, 45));
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private void openLogin(UserRole role) {
        new RoleLoginWindow(role).setVisible(true);
        dispose();
    }
}