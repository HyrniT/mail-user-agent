public class UserModel {
    private String _email;
    private String _password;
    private String _fullname;

    public UserModel() { }

    public UserModel(String email, String password, String fullname) {
        this._email = email;
        this._password = password;
        this._fullname = fullname;
    }

    public String getEmail() {
        return this._email;
    }

    public String getPassword() {
        return this._password;
    }

    public String getFullname() {
        return this._fullname;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public void setFullname(String fullname) {
        this._fullname = fullname;
    }

    public String toString() {
        return this._fullname + " <" + this._email + ">";
    }
}