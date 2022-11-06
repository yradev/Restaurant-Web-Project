package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.LunchMenu;

import java.time.LocalDate;

@Repository
public interface LunchMenuRepository extends JpaRepository<LunchMenu, Long> {
    LunchMenu  findByDateNow(LocalDate date);
}
