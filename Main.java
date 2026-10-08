import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {

        setTitle("Hospital Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("HOSPITAL MANAGEMENT SYSTEM");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 20, 20));

        JButton patientButton = new JButton("Patients");
        JButton doctorButton = new JButton("Doctors");
        JButton appointmentButton = new JButton("Appointments");
        JButton consultationButton = new JButton("Consultations");
        JButton prescriptionButton = new JButton("Prescriptions");
        JButton billingButton = new JButton("Billing");

        panel.add(patientButton);
        panel.add(doctorButton);
        panel.add(appointmentButton);
        panel.add(consultationButton);
        panel.add(prescriptionButton);
        panel.add(billingButton);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Main window = new Main();
        window.setVisible(true);
    }
}