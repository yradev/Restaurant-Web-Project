package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.model.dto.view.LunchMenuViewDTO;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.model.entity.LunchMenu;
import source.restaurant_web_project.repository.ItemRepository;
import source.restaurant_web_project.repository.LunchMenuRepository;
import source.restaurant_web_project.service.LunchMenuService;

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
            return null;
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
    public LunchMenuAddDTO getLunchMenuAddDto() {
        LunchMenu lunchMenu = lunchMenuRepository.findByDateNow(LocalDate.now());

        if(lunchMenu==null){
            return new LunchMenuAddDTO();
        }

        return modelMapper.map(lunchMenu,LunchMenuAddDTO.class);
    }

    @Override
    public List<LunchMenuViewDTO> getLunchHistory() {
        return lunchMenuRepository.findAll().stream()
                .map(lunchMenu->modelMapper.map(lunchMenu,LunchMenuViewDTO.class))
                .filter(lunchMenu -> lunchMenu.getDateNow().isBefore(LocalDate.now()))
                .sorted((a,b)->b.getDateNow().compareTo(a.getDateNow()))
                .collect(Collectors.toList());
    }

    @Override
    public void clearLunchMenuHistory() {
        List<LunchMenu>lunchMenusForClean = lunchMenuRepository.findAll().stream().filter(lunchMenu -> lunchMenu.getDateNow().isBefore(LocalDate.now())).collect(Collectors.toList());
        lunchMenuRepository.deleteAll(lunchMenusForClean);
    }
}
