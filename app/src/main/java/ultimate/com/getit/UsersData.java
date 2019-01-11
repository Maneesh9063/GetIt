package ultimate.com.getit;

public class UsersData {

    private String UName;
    private String Name;
    private String Phone;
    private String Email;
    private String Pass;

    public  UsersData(){}

    public UsersData(String uname, String name, String phone, String email, String pass) {
        UName = uname;
        Name = name;
        Phone = phone;
        Email = email;
        Pass = pass;
    }
    public String getUName() {
        return UName;
    }

    public void setUName(String uname) {
        UName = uname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
