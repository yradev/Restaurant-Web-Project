package source.restaurant_web_project.models.dto.view;

import java.time.LocalDate;

public class NewsViewDTO {
    private long id;
    private LocalDate newsDate;
    private String news;

    public NewsViewDTO(){}

    public LocalDate getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(LocalDate newsDate) {
        this.newsDate = newsDate;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
