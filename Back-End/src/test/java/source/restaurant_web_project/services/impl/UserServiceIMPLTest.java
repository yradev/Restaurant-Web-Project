package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.Conditions;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import source.restaurant_web_project.models.dto.user.AddressDTO;
import source.restaurant_web_project.models.dto.user.RoleDTO;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.dto.user.UserEditDTO;
import source.restaurant_web_project.models.entity.Address;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.AddressRepository;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceIMPLTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private RoleRepository roleRepository;

    private UserServiceIMPL test;

    @BeforeEach
    void setUp() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        test = new UserServiceIMPL(userRepository, modelMapper, bCryptPasswordEncoder, addressRepository, roleRepository);
    }

    @Test
    void getUserData() {
        when(userRepository.findUserByEmail(any())).thenReturn(null);

        Assertions.assertThrows(BadCredentialsException.class, () -> test.getUserData(""));

        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        test.getUserData("");
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    void editUserData() {
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setEmail("valid1");

        User user = new User();
        user.setEmail("valid2");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(userRepository.findUserByEmail("valid1")).thenReturn(user);

        Assertions.assertThrows(BadCredentialsException.class, () -> test.editUserData(userEditDTO));

        when(userRepository.findUserByEmail("valid1")).thenReturn(null);

        userEditDTO.setOldPassword("invalid");
        user.setPassword(new BCryptPasswordEncoder().encode("valid"));

        Assertions.assertThrows(BadCredentialsException.class, () -> test.editUserData(userEditDTO));

        userEditDTO.setOldPassword("valid");
        userEditDTO.setNewPassword("pass");
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("1");
        addressDTO.setId(0);

        userEditDTO.setAddress(List.of(addressDTO));

        Address address = new Address();
        address.setId(2);

        when(addressRepository.saveAndFlush(any())).thenReturn(address);
        test.editUserData(userEditDTO);
    }

    @Test
    void getActiveRoles() {
        Role role = new Role();
        role.setName("name");
        when(roleRepository.findAll()).thenReturn(List.of(role));

        Assertions.assertEquals(test.getActiveRoles().get(0).getName(), "name");
    }

    @Test
    void changeUserCoreData() {
        UserControlDTO userControlDTO = new UserControlDTO();

        when(userRepository.findUserByEmail(any())).thenReturn(null);

        User user = new User();

        Assertions.assertThrows(BadCredentialsException.class, () -> test.changeUserCoreData(any(), userControlDTO));

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        Assertions.assertThrows(BadCredentialsException.class, () -> test.changeUserCoreData(any(), userControlDTO));


        List<RoleDTO> roleDTOS = List.of(new RoleDTO());
        roleDTOS.get(0).setName("ROLE_USER");
        userControlDTO.setRoles(roleDTOS);

        when(roleRepository.findByName(any())).thenReturn(null);

        Assertions.assertThrows(BadCredentialsException.class, () -> test.changeUserCoreData(any(), userControlDTO));

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName(any())).thenReturn(role);

        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);

        userControlDTO.setEnabled(true);

        user.setEnabled(false);
        test.changeUserCoreData(any(),userControlDTO);
        verify(userRepository,times(1)).saveAndFlush(any());

        userControlDTO.setEnabled(false);

        user.setEnabled(true);
        test.changeUserCoreData(any(),userControlDTO);
        verify(userRepository,times(2)).saveAndFlush(any());

    }

    @Test
    void getUserCoreSettings() {
        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(null);

        Assertions.assertThrows(BadCredentialsException.class,()->test.getUserCoreSettings(any()));

        user.setEmail("email");
        user.setRoles(new ArrayList<>());
        Role role = new Role();
        role.setName("ROLE_USER");
        user.getRoles().add(role);
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        Assertions.assertEquals(test.getUserCoreSettings("email").getRoles().get(0).getName(),"ROLE_USER");

    }
}