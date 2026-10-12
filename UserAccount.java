
public class UserAccount {

    private int userId;
    private String username;
    private String passwordHash;
    private UserRole role;
    private Integer patientId;
    private Integer doctorId;
    private boolean active;

    public UserAccount(
            int userId,
            String username,
            String passwordHash,
            UserRole role,
            Integer patientId,
            Integer doctorId,
            boolean active) {

        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.active = active;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public boolean isActive() {
        return active;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
