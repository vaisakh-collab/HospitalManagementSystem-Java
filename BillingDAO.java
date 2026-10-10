import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {
    
    // 1. Add a new bill
    public boolean addBill(int patientId, double amount, String status) {
        String query = "INSERT INTO Billing (patient_id, amount, status) VALUES (?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
             
            pst.setInt(1, patientId);
            pst.setDouble(2, amount);
            pst.setString(3, status);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Fetch history data
    public List<Object[]> getAllBills() {
        List<Object[]> billsList = new ArrayList<>();
        String query = "SELECT bill_id, patient_id, amount, status FROM Billing";
        
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
             
            while (rs.next()) {
                billsList.add(new Object[]{
                    rs.getInt("bill_id"),
                    rs.getInt("patient_id"),
                    "₹" + rs.getDouble("amount"),
                    rs.getString("status")
                });
            }
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return billsList;
    }

    // 3. Delete a bill
    public boolean deleteBill(int billId) {
        String query = "DELETE FROM Billing WHERE bill_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
             
            pst.setInt(1, billId);
            return pst.executeUpdate() > 0;
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Update bill status
    public boolean updateBillStatus(int billId, String newStatus) {
        String query = "UPDATE Billing SET status = ? WHERE bill_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
             
            pst.setString(1, newStatus);
            pst.setInt(2, billId);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}