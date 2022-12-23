package source.restaurant_web_project.models.dto.news;

import source.restaurant_web_project.models.entity.enums.NewsStatus;

import javax.validation.constraints.Size;

public class AddNewNewsDTO {
    @Size(max = 60,message = "News must be max 60 chars!")
    private String news;
    private NewsStatus status;

    public AddNewNewsDTO(){}

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public NewsStatus getStatus() {
        return status;
    }

    public void setStatus(NewsStatus status) {
        this.status = status;
    }
}
