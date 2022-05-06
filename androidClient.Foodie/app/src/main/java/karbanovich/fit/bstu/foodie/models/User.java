package karbanovich.fit.bstu.foodie.models;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String registrationDate;
    private String birthday;
    private String phoneNumber;
    private String updateStatus;

    public User(int id, String email, String firstName, String lastName, String registrationDate, String birthday, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationDate = registrationDate;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
    }

    public User() { }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRegistrationDate() { return registrationDate; }
    public String getBirthday() { return birthday; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUpdateStatus() { return updateStatus; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setUpdateStatus(String updateStatus) { this.updateStatus = updateStatus; }
}
