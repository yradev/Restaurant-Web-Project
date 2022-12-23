package source.restaurant_web_project.models.dto.address;

public class AddressVIewDTO {
    private String name;
    private String city;
    private String village;
    private String street;
    private int number;
    private String entrance;
    private int floor;
    private int apartmentNumber;

    public AddressVIewDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        return String.format("%s (%s %s %s %s %s %s %s)",
                name!=null?name:"",
                city!=null?city:"",
                village!=null?village:"",
                street!=null?street:"",
                number>0?number:"",
                entrance!=null?entrance:"",
                floor>0?floor:"",
                apartmentNumber>0?apartmentNumber:"");
    }
}
