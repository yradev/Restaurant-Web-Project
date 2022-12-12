package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import source.restaurant_web_project.models.dto.user.*;
import source.restaurant_web_project.models.entity.Address;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;
import source.restaurant_web_project.repositories.AddressRepository;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.UserService;
import source.restaurant_web_project.util.DataValidator;

import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DataValidator dataValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, DataValidator dataValidator, BCryptPasswordEncoder bCryptPasswordEncoder, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().equals("") || a.getSource().toString().equals("0")));
        this.dataValidator = dataValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public User getUser(String name) {
        return userRepository.findUserByUsername(name);
    }

    @Override
    public void changeEmail(UserChangeEmailDTO userSettingsDTO, String name) {
        User user = userRepository.findUserByUsername(name);
        modelMapper.map(userSettingsDTO,user);

        userRepository.save(user);
    }

    @Override
    public void changeSettings(UserPasswordChangeDTO userSettingsDTO, String name) {
        User user = userRepository.findUserByUsername(name);
        modelMapper.map(userSettingsDTO,user);

        userRepository.save(user);
    }


    @Override
    public UserViewSettingsDTO getUserSettings(String name) {
        return modelMapper.map(userRepository.findUserByUsername(name),UserViewSettingsDTO.class);
    }

    @Override
    public boolean verifyUserByEmail(String email) {
        return userRepository.findUserByEmail(email) == null;
    }

    @Override
    public BindingResult validateUserChangeEmail(UserChangeEmailDTO userChangeEmailDTO,BindingResult bindingResult, String name) {

        dataValidator.validateEmail(userChangeEmailDTO,bindingResult);

        return bindingResult;
    }

    @Override
    public BindingResult validateUserChangePassword(UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult, String name) {
        User currentUser = userRepository.findUserByUsername(name);
        bindingResult = dataValidator.validatePasswordChange(userPasswordChangeDTO,bindingResult,currentUser.getPassword());
       return bindingResult;
    }

    @Override
    public void changePassword(UserPasswordChangeDTO userPasswordChangeDTO, String name) {
        String encryptedNewPassword = bCryptPasswordEncoder.encode(userPasswordChangeDTO.getPassword());
        User currentLoggedUser = userRepository.findUserByUsername(name);
        currentLoggedUser.setPassword(encryptedNewPassword);
        userRepository.saveAndFlush(currentLoggedUser);
    }

    @Override
    public BindingResult validateAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String username) {
        User user = userRepository.findUserByUsername(username);

        return dataValidator.validateAddressAdd(bindingResult,userAddressSettingsDTO,user.getAddress());
    }

    @Override
    public void addAddress(UserAddressSettingsDTO userAddressSettingsDTO, String name) {
        User user = userRepository.findUserByUsername(name);
        Address address = modelMapper.map(userAddressSettingsDTO,Address.class);
        address.setUser(user);
        addressRepository.saveAndFlush(address);
    }

    @Override
    public void editAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String name) {
        Long id = userRepository.findUserByUsername(name).getAddress().stream()
                .filter(a->a.getName()
                        .equals(userAddressSettingsDTO.getCurrentName()))
                .map(BaseEntity::getId)
                .findFirst().orElse(null);

        Address address = addressRepository.findAddressById(id);
       modelMapper.map(userAddressSettingsDTO,address);
       User user = userRepository.findUserByUsername(name);
       address.setUser(user);
       addressRepository.saveAndFlush(address);
    }

    @Override
    public UserControlDTO findUserForUserControl(String username) {
        User user = userRepository.findUserByUsername(username);
        return user==null?null:modelMapper.map(user,UserControlDTO.class);
    }

    @Override
    public void addRole(String username, String role) {
        User user = userRepository.findUserByUsername(username);
        user.getRoles().add(roleRepository.findByName(role));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeRole(String username, String role) {
        User user = userRepository.findUserByUsername(username);
        user.setRoles(user.getRoles().stream().filter(currentRole->!currentRole.getName().equals(role)).collect(Collectors.toList()));
        userRepository.saveAndFlush(user);
    }
}
