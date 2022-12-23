package source.restaurant_web_project.models.dto.delivery;

public class DeliveryItemAdd {
    private String name;
    private long count;

    public DeliveryItemAdd(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
