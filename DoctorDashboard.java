
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;


public class DoctorDashboard extends JFrame {

    private final UserAccount account;
    private final int doctorId;

    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    private final DefaultTableModel appointmentModel = createModel(
        "Appointment ID", "Patient ID", "Patient",
        "Date", "Time", "Status"
    );

    private final DefaultTableModel consultationModel = createModel(
        "Consultation ID", "Patient ID", "Patient",
        "Date", "Time", "Diagnosis", "Notes"
    );

    public DoctorDashboard(UserAccount account) {
        if (account == null
                || account.getRole() != UserRole.DOCTOR
                || account.getDoctorId() == null) {
            throw new IllegalArgumentException(
                "A linked doctor account is required."
            );
        }

        this.account = account;
        this.doctorId = account.getDoctorId();

        setTitle("Doctor Dashboard");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(245, 248, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel(
            "Welcome, Dr. " + account.getUsername()
        );
        welcome.setFont(new Font("SansSerif", Font.BOLD, 23));
        welcome.setForeground(new Color(35, 45, 65));

        JLabel subtitle = new JLabel(
            "Doctor Dashboard | " + account.getUsername()
        );
        subtitle.setForeground(Color.GRAY);

        JPanel headings = new JPanel(new GridLayout(2, 1, 0, 4));
        headings.setOpaque(false);
        headings.add(welcome);
        headings.add(subtitle);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });

        header.add(headings, BorderLayout.CENTER);
        header.add(logout, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", buildOverviewTab());
        tabs.addTab("My Profile", buildProfileTab());
        tabs.addTab("My Appointments", buildAppointmentsTab());
        tabs.addTab("Consultations", buildConsultationsTab());
        tabs.addTab("Patient History", buildHistoryTab());

        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);

        refreshAll();
    }

    private JPanel buildOverviewTab() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        panel.add(createStatCard("Today's Appointments", "0"));
        panel.add(createStatCard("Total Appointments", "0"));
        panel.add(createStatCard("Total Consultations", "0"));

        JButton refresh = new JButton("Refresh Overview");
        refresh.addActionListener(e -> refreshAll());

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(refresh, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(new Color(235, 242, 252));
        card.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel number = new JLabel(value, SwingConstants.CENTER);
        number.setFont(new Font("SansSerif", Font.BOLD, 28));
        number.setForeground(new Color(48, 112, 190));

        card.add(heading);
        card.add(number);
        return card;
    }

    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        panel.add(new JLabel("Doctor ID:"));
        panel.add(new JLabel(String.valueOf(doctorId)));

        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(account.getUsername()));

        panel.add(new JLabel("Role:"));
        panel.add(new JLabel("Doctor"));

        panel.add(new JLabel("Profile details:"));
        panel.add(new JLabel(
            "Additional details can be loaded from the Doctor record."
        ));

        return panel;
    }

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadAppointments());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(refresh);

        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(appointmentModel)),
                  BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildConsultationsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JButton refresh = new JButton("Refresh");
        JButton record = new JButton("Record Consultation");

        refresh.addActionListener(e -> loadConsultations());
        record.addActionListener(e -> recordConsultation());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(record);
        actions.add(refresh);

        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(consultationModel)),
                  BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildHistoryTab() {
    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBackground(Color.WHITE);
    panel.setBorder(new EmptyBorder(20, 20, 20, 20));

    JButton viewHistory = new JButton("View Patient History");
    viewHistory.addActionListener(e -> viewSelectedHistory());

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
    actions.setBackground(Color.WHITE);
    actions.add(viewHistory);

    panel.add(actions, BorderLayout.NORTH);

    return panel;
}

    
private void recordConsultation() {
    List<Appointment> appointments =
            appointmentDAO.getAppointmentsByDoctor(doctorId);

    if (appointments.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "There are no appointments available to record a consultation.",
                "No Appointments",
                JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    JComboBox<Appointment> appointmentBox = new JComboBox<>();

    for (Appointment appointment : appointments) {
        if (appointment.getPatient() != null) {
            appointmentBox.addItem(appointment);
        }
    }

    appointmentBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            if (value instanceof Appointment) {
                Appointment a = (Appointment) value;
                Patient p = a.getPatient();

                setText(p.getName()
                        + " | " + a.getDate()
                        + " " + a.getTime()
                        + " | Appointment #" + a.getAppointmentId());
            }

            return this;
        }
    });

    JTextField dateField =
            new JTextField(java.time.LocalDate.now().toString());

    JTextField timeField =
            new JTextField(java.time.LocalTime.now()
                    .withSecond(0).withNano(0).toString());

    JTextField diagnosisField = new JTextField();
    JTextArea notesArea = new JTextArea(5, 25);
    notesArea.setLineWrap(true);
    notesArea.setWrapStyleWord(true);

    JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));

    form.add(new JLabel("Appointment / Patient"));
    form.add(appointmentBox);

    form.add(new JLabel("Date (YYYY-MM-DD)"));
    form.add(dateField);

    form.add(new JLabel("Time (HH:MM)"));
    form.add(timeField);

    form.add(new JLabel("Diagnosis"));
    form.add(diagnosisField);

    form.add(new JLabel("Notes"));
    form.add(new JScrollPane(notesArea));

    int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Record Consultation",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    try {
        Appointment appointment =
                (Appointment) appointmentBox.getSelectedItem();

        if (appointment == null || appointment.getPatient() == null) {
            JOptionPane.showMessageDialog(
                    this, "Select a valid appointment.");
            return;
        }

        java.time.LocalDate date =
                java.time.LocalDate.parse(dateField.getText().trim());

        java.time.LocalTime time =
                java.time.LocalTime.parse(timeField.getText().trim());

        String diagnosis = diagnosisField.getText().trim();
        String notes = notesArea.getText().trim();

        if (diagnosis.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Diagnosis cannot be empty.");
            return;
        }

        Patient patient = patientDAO.findPatient(
                appointment.getPatient().getPatientId());

        Doctor doctor = doctorDAO.findDoctor(doctorId);

        if (patient == null || doctor == null) {
            JOptionPane.showMessageDialog(
                    this, "Could not load the patient or doctor record.");
            return;
        }

        Consultation consultation = new Consultation(
                0,
                patient,
                doctor,
                date,
                time,
                diagnosis,
                notes,
                null
        );

        if (consultationDAO.addConsultation(consultation)) {
            JOptionPane.showMessageDialog(
                    this, "Consultation recorded successfully.");

            // Reload the consultation list using your existing method.
            loadConsultations();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the consultation. Check the console.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Could not record consultation:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


    private void viewSelectedHistory() {
    List<Appointment> appointments =
            appointmentDAO.getAppointmentsByDoctor(doctorId);

    if (appointments == null || appointments.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "You have no appointments to select a patient from.",
                "No Patients",
                JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    // Get unique patients from the doctor's appointments.
    java.util.Map<Integer, Patient> patients =
            new java.util.LinkedHashMap<>();

    for (Appointment appointment : appointments) {
        if (appointment == null || appointment.getPatient() == null) {
            continue;
        }

        Patient patient = appointment.getPatient();
        patients.putIfAbsent(patient.getPatientId(), patient);
    }

    if (patients.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "No patients were found in your appointments.",
                "No Patients",
                JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    JComboBox<Patient> patientBox = new JComboBox<>(
            patients.values().toArray(new Patient[0])
    );

    patientBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
            );

            if (value instanceof Patient) {
                Patient patient = (Patient) value;
                setText(
                        patient.getName()
                        + " (Patient ID: "
                        + patient.getPatientId() + ")"
                );
            }

            return this;
        }
    });

    JPanel selectionPanel = new JPanel(new BorderLayout(8, 8));
    selectionPanel.add(
            new JLabel("Select a patient:"),
            BorderLayout.NORTH
    );
    selectionPanel.add(patientBox, BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(
            this,
            selectionPanel,
            "View Patient History",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    Patient selectedPatient = (Patient) patientBox.getSelectedItem();

    if (selectedPatient == null) {
        return;
    }

    // Load the patient's medical history.
    List<Consultation> history =
            patientDAO.getMedicalHistory(
                    selectedPatient.getPatientId()
            );

    if (history == null || history.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "No medical history found for "
                        + selectedPatient.getName() + ".",
                "Medical History",
                JOptionPane.INFORMATION_MESSAGE
        );
        return;
    }

    DefaultTableModel historyModel = createModel(
            "Consultation ID",
            "Date",
            "Time",
            "Diagnosis",
            "Notes"
    );

    for (Consultation consultation : history) {
        historyModel.addRow(new Object[]{
                consultation.getConsultationId(),
                consultation.getDate(),
                consultation.getTime(),
                consultation.getDiagnosis(),
                consultation.getNotes()
        });
    }

    JTable historyTable = new JTable(historyModel);
    historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    historyTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(historyTable);
    scrollPane.setPreferredSize(new Dimension(750, 300));

    JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Medical History — " + selectedPatient.getName(),
            JOptionPane.INFORMATION_MESSAGE
    );
}

    private void refreshAll() {
        loadAppointments();
        loadConsultations();
    }

    private void loadAppointments() {
        appointmentModel.setRowCount(0);

        String sql =
            "SELECT a.appointment_id, a.patient_id, " +
            "p.name AS patient_name, a.date, a.time, a.status " +
            "FROM Appointment a " +
            "JOIN Patient p ON a.patient_id = p.patient_id " +
            "WHERE a.doctor_id = ? ORDER BY a.date, a.time";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointmentModel.addRow(new Object[] {
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("status")
                    });
                }
            }
        } catch (Exception ex) {
            showDatabaseError("appointments", ex);
        }
    }

    private void loadConsultations() {
        consultationModel.setRowCount(0);

        String sql =
            "SELECT c.consultation_id, c.patient_id, " +
            "p.name AS patient_name, c.consultation_date, " +
            "c.consultation_time, c.diagnosis, c.notes " +
            "FROM Consultation c " +
            "JOIN Patient p ON c.patient_id = p.patient_id " +
            "WHERE c.doctor_id = ? " +
            "ORDER BY c.consultation_date DESC, c.consultation_time DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    consultationModel.addRow(new Object[] {
                        rs.getInt("consultation_id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getDate("consultation_date"),
                        rs.getTime("consultation_time"),
                        rs.getString("diagnosis"),
                        rs.getString("notes")
                    });
                }
            }
        } catch (Exception ex) {
            showDatabaseError("consultations", ex);
        }
    }

    private void showDatabaseError(String section, Exception ex) {
        System.err.println("Could not load " + section + ": "
                           + ex.getMessage());

        JOptionPane.showMessageDialog(
            this,
            "Could not load " + section +
            ". Check your database connection and table structure.",
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static DefaultTableModel createModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
