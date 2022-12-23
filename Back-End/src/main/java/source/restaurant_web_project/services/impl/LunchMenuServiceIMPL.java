package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NoContentException;
import source.restaurant_web_project.models.dto.lunchmenu.LunchMenuAddDTO;
import source.restaurant_web_project.models.dto.lunchmenu.LunchMenuViewDTO;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.models.entity.LunchMenu;
import source.restaurant_web_project.models.entity.enums.LunchMenuStatus;
import source.restaurant_web_project.repositories.ItemRepository;
import source.restaurant_web_project.repositories.LunchMenuRepository;
import source.restaurant_web_project.services.LunchMenuService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LunchMenuServiceIMPL implements LunchMenuService {
    private final LunchMenuRepository lunchMenuRepository;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    public LunchMenuServiceIMPL(LunchMenuRepository lunchMenuRepository, ModelMapper modelMapper, ItemRepository itemRepository) {
        this.lunchMenuRepository = lunchMenuRepository;
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public LunchMenuViewDTO getTodayLunchMenu() {
        LunchMenu lunchMenu = lunchMenuRepository.findByDateNow(LocalDate.now());

        if(lunchMenu==null){
            throw new BadRequestException("We dont have lunch menu for today!");
        }

        return modelMapper.map(lunchMenuRepository.findByDateNow(LocalDate.now()), LunchMenuViewDTO.class);
    }

    @Override
    public void addTodayLunchMenu(LunchMenuAddDTO lunchMenuAddDTO) {
        LunchMenu lunchMenu = lunchMenuRepository.findByDateNow(LocalDate.now());
        if(lunchMenu!=null){
            modelMapper.map(lunchMenuAddDTO,lunchMenu);
        }else{
            lunchMenu = modelMapper.map(lunchMenuAddDTO,LunchMenu.class);
        }

        lunchMenu.setDateNow(LocalDate.now());
        lunchMenuRepository.saveAndFlush(lunchMenu);

        Item lunchMenuItem = itemRepository.findItemByName("Lunch menu");
        lunchMenuItem.setPrice(lunchMenuAddDTO.getTotalPrice());
        itemRepository.saveAndFlush(lunchMenuItem);
    }

    @Override
    public List<LunchMenuViewDTO> getOldLunchMenus() {
        return lunchMenuRepository.findAll().stream()
                .map(lunchMenu->modelMapper.map(lunchMenu,LunchMenuViewDTO.class))
                .filter(lunchMenu -> lunchMenu.getDateNow().isBefore(LocalDate.now()))
                .sorted((a,b)->b.getDateNow().compareTo(a.getDateNow()))
                .collect(Collectors.toList());
    }

    @Override
    public void changeStatus(String status) {
        LunchMenu lunchMenu = lunchMenuRepository.findByDateNow(LocalDate.now());
        if(lunchMenu == null){
            throw new NoContentException("We dont have lunch menu for today!");
        }

        lunchMenu.setStatus(LunchMenuStatus.valueOf(status));
    }


}
