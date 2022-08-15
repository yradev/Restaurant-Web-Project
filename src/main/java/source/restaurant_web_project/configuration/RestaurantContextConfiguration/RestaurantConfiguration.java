package source.restaurant_web_project.configuration.RestaurantContextConfiguration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RestaurantConfiguration {
    private final ConfgurationRepository confgurationRepository;
    private final ModelMapper modelMapper;

    public RestaurantConfiguration(ConfgurationRepository confgurationRepository, ModelMapper modelMapper) {
        this.confgurationRepository = confgurationRepository;
        this.modelMapper = modelMapper;
    }

    public RestaurantConfigurationDTO getConfigurations() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().toString().equals("0")));
        return confgurationRepository.findAll().stream().map(configuration->modelMapper.map(configuration,RestaurantConfigurationDTO.class)).findFirst().orElse(null);
    }

    public void editConfigurations(RestaurantConfigurationDTO restaurantConfigurationDTO) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().toString().equals("0")));
        RestaurantConfigurationEntity configuration = confgurationRepository.findAll().stream().findFirst().orElse(null);

        modelMapper.map(restaurantConfigurationDTO,configuration);

        confgurationRepository.save(configuration);
    }
}
