package source.restaurant_web_project.model.dto;

public class UserSettingsDTO {
    private String email;
    private String currentPassword;
    private String password;
    private String confirmPassword;
    private String addressCity;
    private String addressVillage;
    private String addressStreet;
    private int addressNumber;
    private String addressEntrance;
    private int addressFloor;
    private int addressApartmentNumber;

    public UserSettingsDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public String getAddressVillage() {
        return addressVillage;
    }

    public void setAddressVillage(String addressVillage) {
        this.addressVillage = addressVillage;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressEntrance() {
        return addressEntrance;
    }

    public void setAddressEntrance(String addressEntrance) {
        this.addressEntrance = addressEntrance;
    }

    public int getAddressFloor() {
        return addressFloor;
    }

    public void setAddressFloor(int addressFloor) {
        this.addressFloor = addressFloor;
    }

    public int getAddressApartmentNumber() {
        return addressApartmentNumber;
    }

    public void setAddressApartmentNumber(int addressApartmentNumber) {
        this.addressApartmentNumber = addressApartmentNumber;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }
}