package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.news.AddNewNewsDTO;
import source.restaurant_web_project.models.dto.news.NewViewDTO;

import java.util.List;

public interface NewsService {
    long addNews(AddNewNewsDTO addNewNewsDTO);

    List<NewViewDTO> getActiveNews();

    NewViewDTO getNew(long id);

    void deleteNews(long id);

    List<NewViewDTO> getHistoryNews();
}
