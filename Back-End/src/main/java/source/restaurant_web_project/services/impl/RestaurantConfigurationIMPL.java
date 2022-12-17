package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationDTO;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.services.RestaurantConfiguration;

@Service
public class RestaurantConfigurationIMPL implements RestaurantConfiguration {
    private final RestaurantConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;

    public RestaurantConfigurationIMPL(RestaurantConfigurationRepository configurationRepository, ModelMapper modelMapper) {
        this.configurationRepository = configurationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RestaurantConfigurationDTO getData() {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().toString().equals("0")));
        RestaurantConfigurationEntity restaurantConfigurationEntity = configurationRepository.findById(1);
        return modelMapper.map(restaurantConfigurationEntity,RestaurantConfigurationDTO.class);
    }

    @Override
    public void edit(RestaurantConfigurationDTO restaurantConfigurationDTO) {
        RestaurantConfigurationEntity configuration = configurationRepository.findById(1);

        modelMapper.map(restaurantConfigurationDTO,configuration);

        configurationRepository.save(configuration);
    }
}
