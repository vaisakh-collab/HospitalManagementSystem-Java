
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainWindow() {

        // Window settings
        setTitle("Hospital Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(8, 1, 0, 5));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(35, 45, 65));

        JLabel heading = new JLabel("  HOSPITAL SYSTEM");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        sidebar.add(heading);

        // Main content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create pages
        contentPanel.add(
            createPage("Dashboard", "Welcome to the Hospital Management System."),
            "Dashboard"
        );

        contentPanel.add(
            createPage("Patients", "Manage patient information."),
            "Patients"
        );

        contentPanel.add(
            createPage("Doctors", "Manage doctor information."),
            "Doctors"
        );

        contentPanel.add(
            createPage("Appointments", "Book and manage appointments."),
            "Appointments"
        );

        contentPanel.add(
            createPage("Consultations", "View and manage consultations."),
            "Consultations"
        );

        contentPanel.add(
            createPage("Prescriptions", "Manage patient prescriptions."),
            "Prescriptions"
        );

        contentPanel.add(
            createPage("Billing", "Manage bills and payment status."),
            "Billing"
        );

        // Navigation buttons
        String[] pages = {
            "Dashboard",
            "Patients",
            "Doctors",
            "Appointments",
            "Consultations",
            "Prescriptions",
            "Billing"
        };

        for (String page : pages) {
            JButton button = new JButton(page);

            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(35, 45, 65));
            button.setBorderPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.addActionListener(e ->
                cardLayout.show(contentPanel, page)
            );

            sidebar.add(button);
        }

        // Add components to the window
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Display the initial page
        cardLayout.show(contentPanel, "Dashboard");
    }

    // Creates a placeholder panel for each page
    private JPanel createPage(String title, String description) {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(35, 45, 65));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionLabel, BorderLayout.CENTER);

        return panel;
    }
}