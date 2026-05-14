package model;

public class User {

    private int id;

    private String fullName;

    private String email;

    private String passwordHash;

    private Role role;

    private boolean expertRequest;

    private boolean validated;

    private String profilePicture;

    public User() {
    }

    public User(
            int id,
            String fullName,
            String email,
            String passwordHash,
            Role role,
            boolean expertRequest,
            boolean validated,
            String profilePicture
    ) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.expertRequest = expertRequest;
        this.validated = validated;
        this.profilePicture = profilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isExpertRequest() {
        return expertRequest;
    }

    public void setExpertRequest(boolean expertRequest) {
        this.expertRequest = expertRequest;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}