import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PatientDashboard extends JFrame {
    private final UserAccount account;
    private final int patientId;
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    private final DefaultTableModel appointmentModel = model("Appointment ID", "Doctor", "Specialization", "Date", "Time", "Status");
    private final DefaultTableModel historyModel = model("Consultation ID", "Doctor", "Date", "Time", "Diagnosis", "Notes");
    private final DefaultTableModel prescriptionModel = model("Prescription ID", "Doctor", "Medication", "Dosage", "Frequency", "Duration", "Instructions");
    private final DefaultTableModel billModel = model("Bill ID", "Consultation ID", "Amount", "Date", "Status");

    public PatientDashboard(UserAccount account) {
        if (account == null || account.getRole() != UserRole.PATIENT || account.getPatientId() == null) {
            throw new IllegalArgumentException("A linked patient account is required.");
        }
        this.account = account;
        this.patientId = account.getPatientId();

        setTitle("Patient Dashboard");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Patient patient = patientDAO.findPatient(patientId);
        String patientName = patient == null ? account.getUsername() : patient.getName();

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(245, 248, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel welcome = new JLabel("Welcome, " + patientName);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 23));
        welcome.setForeground(new Color(35, 45, 65));
        JLabel subtitle = new JLabel("Patient Dashboard  |  " + account.getUsername());
        subtitle.setForeground(Color.GRAY);
        JPanel headings = new JPanel(new GridLayout(2, 1, 0, 4));
        headings.setOpaque(false); headings.add(welcome); headings.add(subtitle);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });
        header.add(headings, BorderLayout.CENTER);
        header.add(logout, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Profile", buildProfileTab(patient));
        tabs.addTab("My Appointments", buildAppointmentsTab());
        tabs.addTab("Medical History", tablePanel(historyModel));
        tabs.addTab("My Prescriptions", tablePanel(prescriptionModel));
        tabs.addTab("My Bills", buildBillsTab());
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
        refreshAll();
    }

    private JPanel buildProfileTab(Patient patient) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        if (patient == null) {
            panel.add(new JLabel("Could not load your patient profile."));
            return panel;
        }
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 8, 8, 8);
        String[][] values = {
            {"Patient ID", String.valueOf(patient.getPatientId())},
            {"Name", patient.getName()},
            {"Age", String.valueOf(patient.getAge())},
            {"Phone", patient.getPhone()},
            {"Address", patient.getAddress()},
            {"Email", patient.getEmail()}
        };
        for (int i = 0; i < values.length; i++) {
            g.gridy = i;
            JLabel label = new JLabel(values[i][0] + ":");
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            panel.add(label, g);
            g.gridx = 1;
            panel.add(new JLabel(values[i][1] == null ? "" : values[i][1]), g);
            g.gridx = 0;
        }
        return panel;
    }

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton book = new JButton("Request Appointment");
        JButton refresh = new JButton("Refresh");
        book.addActionListener(e -> showBookingDialog());
        refresh.addActionListener(e -> loadAppointments());
        actions.add(book); actions.add(refresh);
        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(appointmentModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel tablePanel(DefaultTableModel model) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshAll());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refresh);
        panel.add(top, BorderLayout.NORTH);
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void showBookingDialog() {
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No doctors are currently available.",
                "Appointment", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<Doctor> doctorCombo = new JComboBox<>();
        for (Doctor d : doctors) doctorCombo.addItem(d);
        doctorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Doctor) {
                    Doctor d = (Doctor) value;
                    setText(d.getName() + " — " + d.getSpecialization() + " (ID " + d.getDoctorId() + ")");
                }
                return this;
            }
        });

        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString(), 12);
        JTextField timeField = new JTextField("10:00", 8);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Doctor")); form.add(doctorCombo);
        form.add(new JLabel("Date (YYYY-MM-DD)")); form.add(dateField);
        form.add(new JLabel("Time (HH:MM)")); form.add(timeField);

        int result = JOptionPane.showConfirmDialog(this, form, "Request Appointment",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            if (doctor == null || !date.isAfter(LocalDate.now()) || time.getSecond() != 0) {
                JOptionPane.showMessageDialog(this, "Choose a doctor, a future date, and a time in HH:MM format.",
                    "Invalid Appointment", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Prevent an exact duplicate slot for the same doctor.
            if (doctorSlotTaken(doctor.getDoctorId(), date, time)) {
                JOptionPane.showMessageDialog(this, "That doctor already has an appointment at this time.",
                    "Slot Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Patient patient = patientDAO.findPatient(patientId);
            Appointment appointment = new Appointment(patient, doctor, date, time);
            if (appointmentDAO.addAppointment(appointment)) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "Could not book the appointment.",
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use date YYYY-MM-DD and time HH:MM.",
                "Invalid Date or Time", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while booking the appointment.",
                "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean doctorSlotTaken(int doctorId, LocalDate date, LocalTime time) throws Exception {
        String sql = "SELECT COUNT(*) FROM Appointment WHERE doctor_id = ? AND date = ? AND time = ? AND status <> 'Cancelled'";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setTime(3, java.sql.Time.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void refreshAll() {
        loadAppointments();
        loadHistory();
        loadPrescriptions();
        loadBills();
    }

    private void loadAppointments() {
        appointmentModel.setRowCount(0);
        for (Appointment a : appointmentDAO.getAppointmentsByPatient(patientId)) {
            String doctorName = a.getDoctor() == null ? "Unknown" : a.getDoctor().getName();
            String specialization = a.getDoctor() == null ? "" : a.getDoctor().getSpecialization();
            appointmentModel.addRow(new Object[] {a.getAppointmentId(), doctorName, specialization,
                a.getDate(), a.getTime(), a.getStatus()});
        }
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        for (Consultation c : patientDAO.getMedicalHistory(patientId)) {
            String doctor = c.getDoctor() == null ? "Unknown" : c.getDoctor().getName();
            historyModel.addRow(new Object[] {c.getConsultationId(), doctor, c.getDate(), c.getTime(),
                c.getDiagnosis(), c.getNotes()});
        }
    }

    private void loadPrescriptions() {
        prescriptionModel.setRowCount(0);
        for (Prescription p : prescriptionDAO.getPrescriptionsByPatient(patientId)) {
            String doctor = p.getDoctor() == null ? "Unknown" : p.getDoctor().getName();
            prescriptionModel.addRow(new Object[] {p.getPrescriptionId(), doctor, p.getMedication(),
                p.getDosage(), p.getFrequency(), p.getDuration(), p.getInstructions()});
        }
    }


    private JPanel buildBillsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JTable billTable = new JTable(billModel);
        billTable.setAutoCreateRowSorter(true);

        JButton payButton = new JButton("Pay Bill");
        JButton refreshButton = new JButton("Refresh");

        payButton.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this, "Please select a bill to pay.");
                return;
            }

            selectedRow = billTable.convertRowIndexToModel(selectedRow);

            int billId = Integer.parseInt(
                    billModel.getValueAt(selectedRow, 0).toString());

            String status = billModel.getValueAt(selectedRow, 4).toString();

            if (!"Pending".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(
                        this, "This bill is not pending.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm payment for Bill ID " + billId + "?",
                    "Confirm Payment",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = "UPDATE Billing SET status = 'PAID' "
                    + "WHERE bill_id = ? AND patient_id = ? "
                    + "AND status = 'UNPAID'";

            try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, billId);
                ps.setInt(2, patientId);

                if (ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(
                            this, "Bill marked as paid successfully.");
                    loadBills();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Payment could not be updated. Refresh and try again.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, "Could not update payment status.");
            }
        });

        refreshButton.addActionListener(e -> loadBills());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(payButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        return panel;
    }


    private void loadBills() {
        billModel.setRowCount(0);
        String sql = "SELECT bill_id, consultation_id, amount, date, status FROM Billing WHERE patient_id = ? ORDER BY date DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    billModel.addRow(new Object[] {rs.getInt("bill_id"), rs.getObject("consultation_id"),
                        rs.getBigDecimal("amount"), rs.getDate("date"), rs.getString("status")});
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load bills. Check the database connection.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }
}
