package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.NoContentException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.news.AddNewNewsDTO;
import source.restaurant_web_project.models.dto.news.NewViewDTO;
import source.restaurant_web_project.models.entity.News;
import source.restaurant_web_project.models.entity.enums.NewsStatus;
import source.restaurant_web_project.repositories.NewsRepository;
import source.restaurant_web_project.services.NewsService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceIMPL implements NewsService {
    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;

    public NewsServiceIMPL(NewsRepository newsRepository, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public long addNews(AddNewNewsDTO addNewNewsDTO) {
        News news = modelMapper.map(addNewNewsDTO,News.class);
        news.setNewsDate(LocalDate.now());
        news.setStatus(NewsStatus.ACTIVE);
        newsRepository.saveAndFlush(news);
        return news.getId();
    }

    @Override
    public List<NewViewDTO> getActiveNews() {
      List<NewViewDTO> news = newsRepository.findByStatus(NewsStatus.ACTIVE).stream()
              .map(a -> modelMapper.map(a, NewViewDTO.class))
              .toList();

      if(news.isEmpty()){
          throw new NoContentException();
      }

      return news;

    }

    @Override
    public NewViewDTO getNew(long id){
        News news = newsRepository.findById(id);
        if(news == null){
            throw new NotFoundException("We dont have new with this id!");
        }
        return new NewViewDTO();
    }

    @Override
    public void deleteNews(long id) {
        newsRepository.delete(newsRepository.findById(id));
    }

    @Override
    public List<NewViewDTO> getHistoryNews() {
        return newsRepository.findByStatus(NewsStatus.CLOSED)
                .stream()
                .map(news->modelMapper.map(news, NewViewDTO.class))
                .collect(Collectors.toList());
    }
}
