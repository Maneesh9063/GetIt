package ultimate.com.getit;

//class used to get the data from the snapshot from fire base

public class updatingClass {

    private String UName;
    private String Name;
    private String Phone;
    private String Amount;
    private long TimeOfRegistration;
    private boolean HandCashRequest;
    private double Latitude,Longitude;
    private  String uid;

    public updatingClass(){}

    public updatingClass(String Uname, String Name, String Phone, String Amount, long TimeOfRegistration, boolean HandCashRequest) {
        this.UName = Uname;
        this.Name = Name;
        this.Phone = Phone;
        this.Amount = Amount;
        this.TimeOfRegistration = TimeOfRegistration;
        this.HandCashRequest = HandCashRequest;
    }

    public String getUName() {
        return UName;
    }

    public boolean getHandCashRequest() {
        return HandCashRequest;
    }

    public void setHandCashRequest(boolean handCashRequest) {
        HandCashRequest = handCashRequest;
    }

    public void setUName(String Uname) {
        this.UName = Uname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        this.Amount = amount;
    }

    public long getTimeOfRegistration() {
        return TimeOfRegistration;
    }

    public void setTimeOfRegistration(long timeOfRegistration) {
        TimeOfRegistration = timeOfRegistration;
    }
    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
