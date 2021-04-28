package transaction;

public class Client {
    private String full_name;
    private String address;
    private String phone;
    private boolean regular;

    public Client(String full_name, String address, String phone, boolean regular) {
        this.full_name = full_name;
        this.address = address;
        this.phone = phone;
        this.regular = regular;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }
}
