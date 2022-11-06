package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.enums.NewsStatus;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class News extends BaseEntity {
    private LocalDate newsDate;
    @Column(columnDefinition = "text")
    private String news;
    private NewsStatus status;

    public News(){}

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

    public LocalDate getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(LocalDate newsDate) {
        this.newsDate = newsDate;
    }
}
