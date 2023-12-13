public class UserModel {
    private String email;
    private String password;
    private String fullname;

    public UserModel() { }

    public UserModel(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String toString() {
        return this.fullname + " <" + this.email + ">";
    }
}