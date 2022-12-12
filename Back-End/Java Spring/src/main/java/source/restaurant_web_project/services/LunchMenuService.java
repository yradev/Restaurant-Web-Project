package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.models.dto.view.LunchMenuViewDTO;

import java.util.List;

public interface LunchMenuService {
    LunchMenuViewDTO getTodayLunchMenu();
    void addTodayLunchMenu(LunchMenuAddDTO lunchMenuAddDTO);

    LunchMenuAddDTO getLunchMenuAddDto();

    List<LunchMenuViewDTO> getLunchHistory();

    void clearLunchMenuHistory();
}
