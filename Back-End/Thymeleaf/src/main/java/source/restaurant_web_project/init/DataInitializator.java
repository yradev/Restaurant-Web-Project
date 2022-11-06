package source.restaurant_web_project.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.repository.ConfgurationRepository;
import source.restaurant_web_project.model.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.configuration.enums.Roles;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.repository.ItemRepository;
import source.restaurant_web_project.repository.RoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataInitializator implements CommandLineRunner {
    private final ConfgurationRepository confgurationRepository;
    private final RoleRepository roleRepository;
    private final ItemRepository itemRepository;

    public DataInitializator(ConfgurationRepository confgurationRepository, RoleRepository roleRepository, ItemRepository itemRepository) {
        this.confgurationRepository = confgurationRepository;
        this.roleRepository = roleRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count()!= Roles.values().length){
            List<String> currentRolesNames = roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
            Arrays.stream(Roles.values())
                    .filter(role -> !currentRolesNames.contains(role.name()))
                    .map(role -> {
                        Role tempRole = new Role();
                        tempRole.setName(role.name());
                        return tempRole;
                    })
                    .forEach(roleRepository::save);
        }

        if(confgurationRepository.count()==0){
            RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();
            restaurantConfigurationEntity.setConfigured(false);
            confgurationRepository.save(restaurantConfigurationEntity);
        }

        if(itemRepository.findItemByName("Lunch menu")==null){
            Item item = new Item();
            item.setName("Lunch menu");
            itemRepository.save(item);
        }
    }
}
