package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.News;
import source.restaurant_web_project.models.entity.enums.NewsStatus;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    List<News>findByStatus(NewsStatus newsStatus);
    News findById(long id);
}
