package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.AddNewNewsDTO;
import source.restaurant_web_project.model.dto.view.NewsViewDTO;
import source.restaurant_web_project.model.entity.News;
import source.restaurant_web_project.model.entity.enums.NewsStatus;
import source.restaurant_web_project.repository.NewsRepository;
import source.restaurant_web_project.service.NewsService;

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
    public void addNews(AddNewNewsDTO addNewNewsDTO) {
        News news = modelMapper.map(addNewNewsDTO,News.class);
        news.setNewsDate(LocalDate.now());
        news.setStatus(NewsStatus.ACTIVE);
        newsRepository.saveAndFlush(news);
    }

    @Override
    public List<NewsViewDTO> getActiveNews() {
        return newsRepository.findByStatus(NewsStatus.ACTIVE).stream().map(news->modelMapper.map(news,NewsViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteNews(int id) {
        newsRepository.delete(newsRepository.findById(id));
    }

    @Override
    public List<NewsViewDTO> getHistoryNews() {
        return newsRepository.findByStatus(NewsStatus.CLOSED).stream().map(news->modelMapper.map(news,NewsViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void clearHistory() {
        newsRepository.deleteAll(newsRepository.findAll().stream().filter(news->news.getStatus().equals(NewsStatus.CLOSED)).collect(Collectors.toList()));
    }
}
