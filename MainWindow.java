
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainWindow extends JFrame {

    private static final Color NAVY = new Color(35, 45, 65);
    private static final Color BLUE = new Color(48, 112, 190);
    private static final Color BG = new Color(245, 248, 252);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final Map<String, DefaultTableModel> tableModels =
            new LinkedHashMap<>();

    private final Map<String, JTable> tables = new LinkedHashMap<>();
    private final Map<String, String> queries = new LinkedHashMap<>();

    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public MainWindow() {
        setTitle("Hospital Management System - Admin");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configureQueries();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);

        JLabel heading = new JLabel("  HOSPITAL SYSTEM");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 17));
        heading.setBorder(new EmptyBorder(22, 8, 22, 8));
        sidebar.add(heading, BorderLayout.NORTH);

        JPanel navigation = new JPanel(new GridLayout(0, 1, 0, 4));
        navigation.setBackground(NAVY);
        navigation.setBorder(new EmptyBorder(0, 10, 15, 10));

        String[] pages = {
                "Dashboard", "Patients", "Doctors", "Appointments",
                "Consultations", "Prescriptions", "Billing",
                "User Accounts"
        };

        for (String page : pages) {
            JButton button = new JButton(page);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setForeground(Color.WHITE);
            button.setBackground(NAVY);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setBorder(
                    BorderFactory.createEmptyBorder(11, 12, 11, 8));

            button.addActionListener(e -> {
                cardLayout.show(contentPanel, page);

                if (tableModels.containsKey(page)) {
                    loadTable(page);
                }

                if (page.equals("Dashboard")) {
                    loadDashboardStats();
                }

            });

            navigation.add(button);
        }

        sidebar.add(navigation, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(15, 22, 15, 22));

        JLabel adminLabel = new JLabel("Administrator");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 21));
        adminLabel.setForeground(NAVY);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });

        topBar.add(adminLabel, BorderLayout.WEST);
        topBar.add(logout, BorderLayout.EAST);

        contentPanel.add(buildDashboard(), "Dashboard");

        for (String page : queries.keySet()) {
            contentPanel.add(buildTablePage(page), page);
        }

        JPanel main = new JPanel(new BorderLayout());
        main.add(topBar, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(main, BorderLayout.CENTER);

        setContentPane(root);

        cardLayout.show(contentPanel, "Dashboard");
        loadDashboardStats();
    }

    private void configureQueries() {
        queries.put("Patients",
                "SELECT patient_id, name, age, phone, address, email "
                        + "FROM Patient");

        queries.put("Doctors",
                "SELECT doctor_id, name, age, phone, address, email, "
                        + "specialization FROM Doctor");

        queries.put("Appointments",
                "SELECT appointment_id, patient_id, doctor_id, date, "
                        + "time, status FROM Appointment "
                        + "ORDER BY date DESC, time DESC");

        queries.put("Consultations",
                "SELECT consultation_id, patient_id, doctor_id, "
                        + "consultation_date, consultation_time, "
                        + "diagnosis, notes FROM Consultation "
                        + "ORDER BY consultation_date DESC");

        queries.put("Prescriptions",
                "SELECT prescription_id, patient_id, doctor_id, "
                        + "medication, dosage, frequency, duration, "
                        + "instructions FROM Prescription");

        queries.put("Billing",
                "SELECT bill_id, patient_id, consultation_id, "
                        + "amount, date, status FROM Billing "
                        + "ORDER BY date DESC");

        queries.put("User Accounts",
                "SELECT user_id, username, role, patient_id, "
                        + "doctor_id, is_active FROM UserAccount");
    }

    // ---------------- Dashboard ----------------

    private JPanel buildDashboard() {
        JPanel page = createPage(
                "Dashboard", "Overview of hospital operations.");

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 14));
        stats.setOpaque(false);

        stats.add(statCard("Patients", "patientsCount"));
        stats.add(statCard("Doctors", "doctorsCount"));
        stats.add(statCard("Appointments", "appointmentsCount"));
        stats.add(statCard("Pending Bills", "pendingBillsCount"));

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);

        center.add(stats, BorderLayout.NORTH);

        JLabel note = new JLabel(
                "<html><div style='padding:15px'>"
                        + "<h2>Hospital Administration</h2>"
                        + "Manage patient and doctor records, appointments, "
                        + "consultations, prescriptions, billing, and "
                        + "user accounts."
                        + "</div></html>");

        note.setOpaque(true);
        note.setBackground(Color.WHITE);
        center.add(note, BorderLayout.CENTER);

        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private JPanel statCard(String title, String key) {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 238)),
                new EmptyBorder(18, 10, 18, 10)));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(NAVY);

        JLabel value = new JLabel("—", SwingConstants.CENTER);
        value.setName(key);
        value.setFont(new Font("Arial", Font.BOLD, 27));
        value.setForeground(BLUE);

        card.add(label);
        card.add(value);
        return card;
    }

    private void loadDashboardStats() {
        setStat("patientsCount", "SELECT COUNT(*) FROM Patient");
        setStat("doctorsCount", "SELECT COUNT(*) FROM Doctor");
        setStat("appointmentsCount", "SELECT COUNT(*) FROM Appointment WHERE status = 'SCHEDULED'");
        setStat("pendingBillsCount",
                "SELECT COUNT(*) FROM Billing WHERE status = 'UNPAID'");
    }

    private void setStat(String key, String sql) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                updateLabel(contentPanel, key,
                        String.valueOf(rs.getInt(1)));
            }

        } catch (Exception ex) {
            updateLabel(contentPanel, key, "—");
            System.err.println(ex.getMessage());
        }
    }

    private void updateLabel(
            Container parent, String key, String value) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JLabel
                    && key.equals(component.getName())) {
                ((JLabel) component).setText(value);
            }

            if (component instanceof Container) {
                updateLabel((Container) component, key, value);
            }
        }
    }

    // ---------------- Tables ----------------

    private JPanel buildTablePage(String pageName) {
        String description = "";

        if (pageName.equals("Patients") || pageName.equals("Doctors")) {
            description = "Select a record to edit. Refresh reloads the database.";
        } else {
            description = "Refresh reloads the database.";
        }

        JPanel page = createPage(pageName, description);

        DefaultTableModel model =
                new DefaultTableModel(getColumns(pageName), 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        tableModels.put(pageName, model);

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(25);
        tables.put(pageName, table);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadTable(pageName));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        actions.add(refresh);

        if (pageName.equals("Patients")
                || pageName.equals("Doctors")) {

            JButton add = new JButton("Add");
            JButton edit = new JButton("Edit");

            add.addActionListener(e -> {
                if (pageName.equals("Patients")) {
                    addPatient();
                } else {
                    addDoctor();
                }
            });

            edit.addActionListener(e -> {
                if (pageName.equals("Patients")) {
                    editPatient(table);
                } else {
                    editDoctor(table);
                }
            });


            actions.add(add);
            actions.add(edit);
        }

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setOpaque(false);
        center.add(actions, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private String[] getColumns(String page) {
        switch (page) {
            case "Patients":
                return new String[]{"Patient ID", "Name", "Age",
                        "Phone", "Address", "Email"};
            case "Doctors":
                return new String[]{"Doctor ID", "Name", "Age",
                        "Phone", "Address", "Email", "Specialization"};
            case "Appointments":
                return new String[]{"Appointment ID", "Patient ID",
                        "Doctor ID", "Date", "Time", "Status"};
            case "Consultations":
                return new String[]{"Consultation ID", "Patient ID",
                        "Doctor ID", "Date", "Time", "Diagnosis", "Notes"};
            case "Prescriptions":
                return new String[]{"Prescription ID", "Patient ID",
                        "Doctor ID", "Medication", "Dosage", "Frequency",
                        "Duration", "Instructions"};
            case "Billing":
                return new String[]{"Bill ID", "Patient ID",
                        "Consultation ID", "Amount", "Date", "Status"};
            default:
                return new String[]{"User ID", "Username", "Role",
                        "Patient ID", "Doctor ID", "Active"};
        }
    }

    private void loadTable(String page) {
        DefaultTableModel model = tableModels.get(page);
        if (model == null) {
            return;
        }

        model.setRowCount(0);

        if (page.equals("Patients")) {
            for (Patient p : patientDAO.getAllPatients()) {
                model.addRow(new Object[]{
                        p.getPatientId(), p.getName(), p.getAge(),
                        p.getPhone(), p.getAddress(), p.getEmail()
                });
            }
            return;
        }

        if (page.equals("Doctors")) {
            for (Doctor d : doctorDAO.getAllDoctors()) {
                model.addRow(new Object[]{
                        d.getDoctorId(), d.getName(), d.getAge(),
                        d.getPhone(), d.getAddress(), d.getEmail(),
                        d.getSpecialization()
                });
            }
            return;
        }

        String sql = queries.get(page);
        if (sql == null) {
            return;
        }

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[count];

                for (int i = 0; i < count; i++) {
                    row[i] = rs.getObject(i + 1);
                }

                model.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load " + page + ".\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- Patients CRUD ----------------

    private void addPatient() {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JTextField phone = new JTextField();
        JTextField address = new JTextField();
        JTextField email = new JTextField();

        JPanel form = createForm(
                new String[]{"Name", "Age", "Phone", "Address", "Email"},
                new JTextField[]{name, age, phone, address, email});

        if (JOptionPane.showConfirmDialog(
                this, form, "Add Patient",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int patientAge = Integer.parseInt(
                    required(age.getText(), "Age"));

            if (patientAge <= 0) {
                throw new IllegalArgumentException(
                        "Age must be greater than zero.");
            }

            Patient patient = new Patient(
                    required(name.getText(), "Name"),
                    patientAge,
                    phone.getText().trim(),
                    address.getText().trim(),
                    email.getText().trim());

            if (patientDAO.addPatient(patient)) {
                success("Patient added successfully.");
                loadTable("Patients");
                loadDashboardStats();
            } else {
                error("Could not add patient.");
            }

        } catch (NumberFormatException ex) {
            error("Age must be a valid integer.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void editPatient(JTable table) {
        int row = selectedModelRow(table);
        if (row == -1) {
            error("Select a patient to edit.");
            return;
        }

        int id = Integer.parseInt(
                table.getModel().getValueAt(row, 0).toString());

        Patient p = patientDAO.findPatient(id);
        if (p == null) {
            error("Patient not found.");
            loadTable("Patients");
            return;
        }

        JTextField name = new JTextField(p.getName());
        JTextField age = new JTextField(String.valueOf(p.getAge()));
        JTextField phone = new JTextField(p.getPhone());
        JTextField address = new JTextField(p.getAddress());
        JTextField email = new JTextField(p.getEmail());

        JPanel form = createForm(
                new String[]{"Name", "Age", "Phone", "Address", "Email"},
                new JTextField[]{name, age, phone, address, email});

        if (JOptionPane.showConfirmDialog(
                this, form, "Edit Patient #" + id,
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int patientAge = Integer.parseInt(
                    required(age.getText(), "Age"));

            if (patientAge <= 0) {
                throw new IllegalArgumentException(
                        "Age must be greater than zero.");
            }

            Patient updated = new Patient(
                    id,
                    required(name.getText(), "Name"),
                    patientAge,
                    phone.getText().trim(),
                    address.getText().trim(),
                    email.getText().trim());

            if (patientDAO.updatePatient(updated)) {
                success("Patient updated successfully.");
                loadTable("Patients");
            } else {
                error("Could not update patient.");
            }

        } catch (NumberFormatException ex) {
            error("Age must be a valid integer.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }


    // ---------------- Doctors CRUD ----------------

    private void addDoctor() {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JTextField phone = new JTextField();
        JTextField address = new JTextField();
        JTextField email = new JTextField();
        JTextField specialization = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        String[] labels = {
                "Name", "Age", "Phone", "Address", "Email",
                "Specialization", "Login Username", "Temporary Password"
        };

        JTextField[] fields = {
                name, age, phone, address, email,
                specialization, username, password
        };

        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }

        if (JOptionPane.showConfirmDialog(
                this, form, "Add Doctor and Login Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }

        char[] passwordChars = password.getPassword();

        try {
            int doctorAge = Integer.parseInt(
                    required(age.getText(), "Age"));

            if (doctorAge <= 0) {
                throw new IllegalArgumentException(
                        "Age must be greater than zero.");
            }

            String loginUsername =
                    required(username.getText(), "Login Username");

            if (passwordChars.length == 0) {
                error("Password cannot be empty.");
                return;
            }

            String loginPassword = new String(passwordChars);

            Doctor doctor = new Doctor(
                    0,
                    required(name.getText(), "Name"),
                    doctorAge,
                    phone.getText().trim(),
                    address.getText().trim(),
                    email.getText().trim(),
                    required(specialization.getText(), "Specialization"));

            if (doctorDAO.addDoctorWithAccount(
                    doctor, loginUsername, loginPassword)) {

                success("Doctor and login account created successfully."
                        + "\nDoctor ID: " + doctor.getDoctorId()
                        + "\nUsername: " + loginUsername);

                loadTable("Doctors");
                loadTable("User Accounts");
                loadDashboardStats();

            } else {
                error("Could not create the doctor account.");
            }

        } catch (NumberFormatException ex) {
            error("Age must be a valid integer.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            error("Could not create doctor and login account.\n"
                    + "Check the console. The username may already exist.");
        } finally {
            Arrays.fill(passwordChars, '\0');
            password.setText("");
        }
    }

    private void editDoctor(JTable table) {
        int row = selectedModelRow(table);
        if (row == -1) {
            error("Select a doctor to edit.");
            return;
        }

        int id = Integer.parseInt(
                table.getModel().getValueAt(row, 0).toString());

        Doctor d = doctorDAO.findDoctor(id);
        if (d == null) {
            error("Doctor not found.");
            loadTable("Doctors");
            return;
        }

        JTextField name = new JTextField(d.getName());
        JTextField age = new JTextField(String.valueOf(d.getAge()));
        JTextField phone = new JTextField(d.getPhone());
        JTextField address = new JTextField(d.getAddress());
        JTextField email = new JTextField(d.getEmail());
        JTextField specialization =
                new JTextField(d.getSpecialization());

        JPanel form = createForm(
                new String[]{"Name", "Age", "Phone", "Address",
                        "Email", "Specialization"},
                new JTextField[]{name, age, phone, address,
                        email, specialization});

        if (JOptionPane.showConfirmDialog(
                this, form, "Edit Doctor #" + id,
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int doctorAge = Integer.parseInt(
                    required(age.getText(), "Age"));

            if (doctorAge <= 0) {
                throw new IllegalArgumentException(
                        "Age must be greater than zero.");
            }

            Doctor updated = new Doctor(
                    id,
                    required(name.getText(), "Name"),
                    doctorAge,
                    phone.getText().trim(),
                    address.getText().trim(),
                    email.getText().trim(),
                    required(specialization.getText(), "Specialization"));

            if (doctorDAO.updateDoctor(updated)) {
                success("Doctor updated successfully.");
                loadTable("Doctors");
            } else {
                error("Could not update doctor.");
            }

        } catch (NumberFormatException ex) {
            error("Age must be a valid integer.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }


    // ---------------- Helpers ----------------

    private JPanel createForm(
            String[] labels, JTextField[] fields) {

        JPanel form = new JPanel(new GridLayout(
                labels.length, 2, 10, 10));

        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            fields[i].setPreferredSize(new Dimension(220, 28));
            form.add(fields[i]);
        }

        return form;
    }

    private int selectedModelRow(JTable table) {
        int selected = table.getSelectedRow();

        if (selected == -1) {
            return -1;
        }

        return table.convertRowIndexToModel(selected);
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    field + " cannot be empty.");
        }

        return value.trim();
    }

    private void success(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createPage(String title, String description) {
        JPanel page = new JPanel(new BorderLayout(14, 14));
        page.setBackground(BG);
        page.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel heading = new JPanel(new GridLayout(2, 1, 0, 5));
        heading.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(NAVY);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(Color.GRAY);

        heading.add(titleLabel);
        heading.add(descriptionLabel);
        page.add(heading, BorderLayout.NORTH);

        return page;
    }
}
