package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.lunchmenu.LunchMenuAddDTO;
import source.restaurant_web_project.models.dto.lunchmenu.LunchMenuViewDTO;

import java.util.List;

public interface LunchMenuService {
    LunchMenuViewDTO getTodayLunchMenu();
    void addTodayLunchMenu(LunchMenuAddDTO lunchMenuAddDTO);
    List<LunchMenuViewDTO> getOldLunchMenus();

    void changeStatus(String status);
}
