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

    public String get_email() {
        return this._email;
    }

    public String get_password() {
        return this._password;
    }

    public String get_fullname() {
        return this._fullname;
    }

    public void set_email(String email) {
        this._email = email;
    }

    public void set_password(String password) {
        this._password = password;
    }

    public void set_fullname(String fullname) {
        this._fullname = fullname;
    }

    public String toString() {
        return this._fullname + " <" + this._email + ">";
    }
}