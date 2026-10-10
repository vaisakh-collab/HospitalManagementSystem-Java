import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingWindow extends JFrame {
    private static final Color NAVY = new Color(28, 48, 74);
    private static final Color BLUE = new Color(48, 112, 190);
    private static final Color RED = new Color(231, 76, 60);
    private static final Color GREEN = new Color(46, 204, 113);
    private static final Color BG_COLOR = new Color(245, 248, 252);

    private DefaultTableModel tableModel;
    private JTable table;
    
    // Dynamic labels
    private JLabel totalRevLabel = new JLabel("₹0.00");
    private JLabel collectedLabel = new JLabel("₹0.00");
    private JLabel pendingLabel = new JLabel("₹0.00");

    public BillingWindow() {
        setTitle("Billing & Invoices");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBorder(new EmptyBorder(20, 24, 20, 24));
        root.setBackground(BG_COLOR);
        setContentPane(root);

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Billing & Invoices");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton markPaidBtn = new JButton("Mark as Paid");
        markPaidBtn.setBackground(GREEN);
        markPaidBtn.setForeground(Color.WHITE);
        markPaidBtn.setFocusPainted(false);
        markPaidBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        markPaidBtn.addActionListener(e -> markSelectedAsPaid());

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(RED);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.addActionListener(e -> deleteSelectedBill());

        JButton newBillBtn = new JButton("+ New Bill");
        newBillBtn.setBackground(BLUE);
        newBillBtn.setForeground(Color.WHITE);
        newBillBtn.setFocusPainted(false);
        newBillBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        newBillBtn.addActionListener(e -> openNewBillDialog());

        buttonsPanel.add(markPaidBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(newBillBtn);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        root.add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.add(createSummaryCard("Total Revenue", totalRevLabel));
        cardsPanel.add(createSummaryCard("Collected", collectedLabel));
        cardsPanel.add(createSummaryCard("Pending", pendingLabel));
        centerPanel.add(cardsPanel, BorderLayout.NORTH);

        // Data Table
        String[] columns = {"Bill ID", "Patient ID", "Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 229, 238)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        root.add(centerPanel, BorderLayout.CENTER);

        // Load History Data on startup
        loadHistoryData();
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 229, 238)),
                new EmptyBorder(16, 16, 16, 16)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(100, 112, 128));
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        valueLabel.setForeground(NAVY);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadHistoryData() {
        tableModel.setRowCount(0); 
        BillingDAO dao = new BillingDAO();
        List<Object[]> bills = dao.getAllBills();
        
        for (Object[] row : bills) {
            tableModel.addRow(row);
        }
        updateMetrics(); 
    }

    private void updateMetrics() {
        double total = 0, collected = 0, pending = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String amountStr = tableModel.getValueAt(i, 2).toString().replace("₹", "");
            double amount = Double.parseDouble(amountStr);
            String status = tableModel.getValueAt(i, 3).toString();
            
            total += amount;
            if (status.equals("Paid")) {
                collected += amount;
            } else if (status.equals("Pending")) {
                pending += amount;
            }
        }
        
        totalRevLabel.setText(String.format("₹%.2f", total));
        collectedLabel.setText(String.format("₹%.2f", collected));
        pendingLabel.setText(String.format("₹%.2f", pending));
    }

    private void markSelectedAsPaid() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentStatus = tableModel.getValueAt(selectedRow, 3).toString();
        if (currentStatus.equals("Paid")) {
            JOptionPane.showMessageDialog(this, "This bill is already marked as Paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int billId = (int) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Mark Bill ID " + billId + " as Paid?", 
            "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            BillingDAO dao = new BillingDAO();
            if (dao.updateBillStatus(billId, "Paid")) {
                loadHistoryData(); // Reloads table and updates the pending/collected totals automatically
                JOptionPane.showMessageDialog(this, "Bill updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bill.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedBill() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int billId = (int) tableModel.getValueAt(selectedRow, 0); 
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete Bill ID " + billId + "?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            BillingDAO dao = new BillingDAO();
            if (dao.deleteBill(billId)) {
                loadHistoryData(); 
                JOptionPane.showMessageDialog(this, "Bill deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill from database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openNewBillDialog() {
        JDialog dialog = new JDialog(this, "Create New Bill", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        JPanel padding = new JPanel(new GridLayout(4, 2, 10, 15));
        padding.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField patientIdField = new JTextField();
        JTextField amountField = new JTextField();
        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Pending", "Paid"});
        
        JButton saveButton = new JButton("Save Bill");
        saveButton.setBackground(BLUE);
        saveButton.setForeground(Color.WHITE);

        padding.add(new JLabel("Patient ID (e.g., 1):"));
        padding.add(patientIdField);
        padding.add(new JLabel("Amount (₹):"));
        padding.add(amountField);
        padding.add(new JLabel("Status:"));
        padding.add(statusDropdown);
        padding.add(new JLabel("")); 
        padding.add(saveButton);
        
        dialog.add(padding, BorderLayout.CENTER);

        saveButton.addActionListener(e -> {
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String status = statusDropdown.getSelectedItem().toString();

                BillingDAO dao = new BillingDAO();
                boolean success = dao.addBill(patientId, amount, status);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Bill saved to database!");
                    loadHistoryData(); 
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save. Ensure Patient ID exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter valid numbers for ID and Amount.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}