package source.restaurant_web_project.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.configuration.enums.Roles;
import source.restaurant_web_project.model.entity.Category;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.repository.CategoryRepository;
import source.restaurant_web_project.repository.RoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataInitializator implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private  final RoleRepository roleRepository;

    public DataInitializator(CategoryRepository categoryRepository, RoleRepository roleRepository) {
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
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
    }
}
