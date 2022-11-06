package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.AddNewNewsDTO;
import source.restaurant_web_project.model.dto.view.NewsViewDTO;

import java.util.List;

public interface NewsService {
    void addNews(AddNewNewsDTO addNewNewsDTO);

    List<NewsViewDTO> getActiveNews();

    void deleteNews(int id);

    List<NewsViewDTO> getHistoryNews();

    void clearHistory();
}
