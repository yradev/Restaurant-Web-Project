package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.ui.Model;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationDTO;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.services.RestaurantConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantConfigurationIMPLTest {

    @Mock
    private RestaurantConfigurationRepository restaurantConfigurationRepository;

    @Mock
    private ModelMapper modelMapper;

    private RestaurantConfigurationIMPL testing;

    @BeforeEach
    void SetUp(){
        testing = new RestaurantConfigurationIMPL(restaurantConfigurationRepository,modelMapper);
    }

    @Test
    void getData() {

        RestaurantConfigurationEntity restaurantConfiguration = new RestaurantConfigurationEntity();
        RestaurantConfigurationDTO restaurantConfigurationDTO = new RestaurantConfigurationDTO();
        when(modelMapper.getConfiguration()).thenReturn(mock(Configuration.class));
        when(modelMapper.map(any(),any())).thenReturn(restaurantConfigurationDTO);
        when(restaurantConfigurationRepository.findById(1)).thenReturn(restaurantConfiguration);
        testing.getData();
        verify(modelMapper,times(1)).map(any(),any());
    }

    @Test
    void edit() {
       RestaurantConfigurationDTO restaurantConfigurationDTO = new RestaurantConfigurationDTO();
       RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();
        when(restaurantConfigurationRepository.findById(1)).thenReturn(restaurantConfigurationEntity);

        testing.edit(restaurantConfigurationDTO);
        verify(restaurantConfigurationRepository,times(1)).save(any());
    }
}