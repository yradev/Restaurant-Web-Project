package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.AddNewNewsDTO;
import source.restaurant_web_project.models.dto.view.NewsViewDTO;

import java.util.List;

public interface NewsService {
    void addNews(AddNewNewsDTO addNewNewsDTO);

    List<NewsViewDTO> getActiveNews();

    void deleteNews(int id);

    List<NewsViewDTO> getHistoryNews();

    void clearHistory();
}
