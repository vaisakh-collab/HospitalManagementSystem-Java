import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final Color NAVY = new Color(28, 48, 74);
    private static final Color BLUE = new Color(48, 112, 190);

    public MainWindow() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        root.setBackground(new Color(245, 248, 252));
        setContentPane(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(22, 24, 22, 24));
        JLabel title = new JLabel("Hospital Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        JLabel subtitle = new JLabel("Manage hospital records from one place");
        subtitle.setForeground(new Color(220, 230, 242));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel headings = new JPanel(new GridLayout(2, 1, 0, 5));
        headings.setOpaque(false);
        headings.add(title);
        headings.add(subtitle);
        header.add(headings, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        JLabel section = new JLabel("Modules");
        section.setFont(new Font("SansSerif", Font.BOLD, 20));
        section.setForeground(NAVY);
        content.add(section, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(0, 2, 16, 16));
        cards.setOpaque(false);
        addModule(cards, "Patients", "Register and manage patient details.");
        addModule(cards, "Doctors", "View and manage doctor information.");
        addModule(cards, "Appointments", "Schedule and manage appointments.");
        addModule(cards, "Consultations", "Maintain consultation records.");
        addModule(cards, "Prescriptions", "Manage patient prescriptions.");
        addModule(cards, "Billing", "Create bills and track payment status.");
        content.add(cards, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);

        JLabel footer = new JLabel("Java Swing  |  Hospital Management System");
        footer.setForeground(new Color(100, 112, 128));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        root.add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addModule(JPanel parent, String name, String description) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 229, 238)),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel heading = new JLabel(name);
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(NAVY);
        JLabel detail = new JLabel("<html><div style='width:260px'>" + description + "</div></html>");
        detail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        detail.setForeground(new Color(90, 101, 116));
        JButton openButton = new JButton("Open " + name);
        openButton.setBackground(BLUE);
        openButton.setForeground(Color.WHITE);
        openButton.setFocusPainted(false);
        
        // Updated Action Listener: Opens BillingWindow for Billing, shows popup for others
        openButton.addActionListener(e -> {
            if (name.equals("Billing")) {
                new BillingWindow().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        name + " module selected.\nDatabase operations require MySQL to be configured.",
                        name,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 6));
        text.setOpaque(false);
        text.add(heading);
        text.add(detail);
        card.add(text, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);
        parent.add(card);
    }
}