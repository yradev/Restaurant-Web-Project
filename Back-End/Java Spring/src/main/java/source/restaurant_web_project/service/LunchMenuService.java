package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.model.dto.view.LunchMenuViewDTO;

import java.util.List;

public interface LunchMenuService {
    LunchMenuViewDTO getTodayLunchMenu();
    void addTodayLunchMenu(LunchMenuAddDTO lunchMenuAddDTO);

    LunchMenuAddDTO getLunchMenuAddDto();

    List<LunchMenuViewDTO> getLunchHistory();

    void clearLunchMenuHistory();
}
