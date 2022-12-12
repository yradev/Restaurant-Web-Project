package source.restaurant_web_project.models.dto.item;

public class CategoryEditDTO {
    private String currentName;
    private String name;
    private long position;

    public CategoryEditDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

}
