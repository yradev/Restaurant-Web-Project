package source.restaurant_web_project.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.model.dto.configuration.RestaurantConfigurationDTO;
import source.restaurant_web_project.model.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repository.ConfgurationRepository;

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

        configuration.setConfigured(true);

        confgurationRepository.save(configuration);
    }
}
