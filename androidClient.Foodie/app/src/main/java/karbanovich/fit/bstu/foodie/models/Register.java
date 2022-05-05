package karbanovich.fit.bstu.foodie.models;
import java.util.Date;

public class Register {
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String phoneNumber;
    private String password;

    public Register(String email, String firstName, String lastName, String birthday, String phoneNumber, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return birthday; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }
}
